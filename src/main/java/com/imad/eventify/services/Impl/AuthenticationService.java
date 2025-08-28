package com.imad.eventify.services.Impl;

import com.imad.eventify.model.DTOs.AuthenticationRequest;
import com.imad.eventify.model.DTOs.AuthenticationResponse;
import com.imad.eventify.model.DTOs.RegisterRequest;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.repositories.UserRepository;
import com.imad.eventify.security.JwtService;
import com.imad.eventify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        // Verify the email if it is already present
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = userService.createUser(registerRequest);

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token, user.getName(), user.getRole());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token, user.getName(), user.getRole());
    }
}
