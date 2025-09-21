package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.InactiveUserException;
import com.imad.eventify.model.DTOs.AuthenticationRequest;
import com.imad.eventify.model.DTOs.AuthenticationResponse;
import com.imad.eventify.model.DTOs.RegisterRequest;
import com.imad.eventify.model.entities.EmailOtp;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.repositories.EmailOtpRepository;
import com.imad.eventify.repositories.UserRepository;
import com.imad.eventify.security.JwtService;
import com.imad.eventify.services.EmailService;
import com.imad.eventify.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailOtpRepository emailOtpRepository;
    private final EmailService emailService;

    /**
     * User Registration: creates an unverified account and sends OTP
     * User Registration Endpoint Scenarios:
     *
     * 1. New user (email not in use):
     *    - A new user is created with verified = false.
     *    - An OTP is generated and sent to the user's email.
     *    - Response: OTP sent to your email. Please verify before login. (no token yet).
     *    - front-end should call the verifyOtp-Endpoint
     *
     * 2. Email already in use (verified user exists):
     *    - Throws IllegalArgumentException("Email already in use").
     *
     * 3. Email exists but user is not verified:
     *    - The OTP verification process should be triggered again.
     *    - A new OTP may be generated and sent.
     *    - front-end should redirect user to New-OTP verification page
     *
     * This ensures duplicate verified users cannot exist, while unverified users
     * are guided to complete verification before using the system.
     */
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        // Check if user with this email already exists
        Optional<User> existingUserOpt = userRepository.findByEmail(registerRequest.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (existingUser.isVerified()) {
                // Verified user exists → cannot register again
                throw new IllegalArgumentException("Email already in use");
            } else {
                // User exists but not verified → resend OTP
                String otp = String.valueOf(100000 + new Random().nextInt(900000));

                EmailOtp emailOtp = new EmailOtp();
                emailOtp.setUser(existingUser);
                emailOtp.setOtpCode(otp);
                emailOtp.setExpiresAt(LocalDateTime.now().plusHours(24));
                emailOtp.setUsed(false);
                emailOtpRepository.save(emailOtp);

                try {
                    emailService.sendEmail(existingUser.getEmail(),
                            "Verify your Eventify account",
                            "Your New OTP is: " + otp + "\nValid for 24 hours.");
                } catch (MessagingException e) {
                    throw new RuntimeException("Failed to send OTP email", e);
                }

                return new AuthenticationResponse(
                        null,
                        existingUser.getName(),
                        existingUser.getRole(),
                        "Account exists but not verified. A new OTP has been sent to your email."
                );
            }
        }

        // Create new user with verified = false
        User newUser = userService.createUser(registerRequest);
        newUser.setVerified(false);
        userRepository.save(newUser);

        // Generate OTP for the new user
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setUser(newUser);
        emailOtp.setOtpCode(otp);
        emailOtp.setExpiresAt(LocalDateTime.now().plusHours(24));
        emailOtp.setUsed(false);
        emailOtpRepository.save(emailOtp);

        try {
            emailService.sendEmail(newUser.getEmail(),
                    "Verify your Eventify account",
                    "Your OTP is: " + otp + "\nValid for 24 hours.");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }

        return new AuthenticationResponse(
                null,
                newUser.getName(),
                newUser.getRole(),
                "OTP sent to your email. Please verify before login."
        );
    }



    /**
     * User Login: only verified users can log in
     * User Authentication Endpoint Scenarios:
     *
     * 1. Valid credentials & verified + active user:
     *    - Authentication succeeds.
     *    - A JWT token is generated and returned in the response.
     *
     * 2. Invalid credentials (wrong email or password):
     *    - Throws IllegalArgumentException("Invalid email or password").
     *
     * 3. Inactive user (user.isActive() == false):
     *    - Throws InactiveUserException("Sorry, Inactive user").
     *
     * 4. Unverified user (user.isVerified() == false):
     *    - Throws IllegalStateException("Please verify your email before login.").
     *    - The front-end should redirect the user to the OTP verification page and call (verifyOtp) Endpoint
     *
     * This ensures only active and verified users can log in and receive a JWT.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.isActive()){
            throw new InactiveUserException("Sorry , Inactive user");
        }
        if (!user.isVerified()) {
            throw new IllegalStateException("Please verify your email before login.");
        }

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token, user.getName(), user.getRole(),"Welcome");
    }



    /**
     * Verify OTP Endpoint Scenarios:
     *
     * 1. Valid OTP (not expired, not used):
     *    - The OTP is marked as used.
     *    - The user account is marked as verified.
     *    - A JWT token is generated and returned in the response.
     *
     * 2. Invalid OTP (wrong code or already used):
     *    - An error is returned with the message "Invalid OTP".
     *
     * 3. Expired OTP:
     *    - A new OTP is generated and stored in the database.
     *    - The new OTP is sent to the user's email.
     *    - An error is returned with the message "OTP expired. A new code has been sent to your email."
     *    - front-end should open the OTP-page again to enter the newOtp
     *
     * This ensures that only verified users can access the system while
     * providing a smooth and secure verification flow.
     */
    public AuthenticationResponse verifyOtp(String email, String otpCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        EmailOtp otp = emailOtpRepository.findByUserAndOtpCodeAndUsedFalse(user, otpCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            // generate new OTP
            EmailOtp newOtp = EmailOtp.builder()
                    .user(user)
                    .otpCode(String.valueOf(100000 + new Random().nextInt(900000)))
                    .expiresAt(LocalDateTime.now().plusHours(24))
                    .used(false)
                    .build();

            emailOtpRepository.save(newOtp);

            try {
                emailService.sendEmail(
                        user.getEmail(),
                        "Your new OTP code",
                        "Your OTP code has expired. Here is a new one: " + newOtp.getOtpCode()
                );
            } catch (MessagingException e) {
                throw new RuntimeException("OTP expired. Failed to send OTP email");
            }

            throw new IllegalStateException("OTP expired. A new code has been sent to your email.");
        }

        otp.setUsed(true);
        emailOtpRepository.save(otp);

        user.setVerified(true);
        userRepository.save(user);

        // generate Token after the verification
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token, user.getName(), user.getRole(), "Email verified successfully");
    }

    /** Resend button
     * Resend OTP for email verification.
     *
     * This method allows a user to request a new OTP before entering any code,
     * or if they want a fresh OTP without using the previous one.
     *
     * Scenarios:
     * 1. User exists and not verified:
     *    - Generates a new OTP and stores it in the database.
     *    - Sends the OTP via email.
     *    - Returns a message: "A new OTP has been sent to your email."
     *
     * 2. User does not exist:
     *    - Throws IllegalArgumentException("User not found").
     *
     * 3. User already verified:
     *    - Throws IllegalStateException("User already verified").
     *
     * Notes:
     * - Always generates a new OTP, independent of any existing OTPs.
     * - Frontend should allow user to request a new OTP freely.
     */
    public void resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isVerified()) {
            throw new IllegalStateException("User already verified");
        }

        // Generate new OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        EmailOtp newOtp = EmailOtp.builder()
                .user(user)
                .otpCode(otp)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();

        emailOtpRepository.save(newOtp);

        // Send OTP email
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    "Your new OTP code",
                    "Here is your new OTP: " + otp
            );
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }


}