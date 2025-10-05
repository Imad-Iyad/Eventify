package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.AuthenticationRequest;
import com.imad.eventify.model.DTOs.AuthenticationResponse;
import com.imad.eventify.model.DTOs.RegisterRequest;
import com.imad.eventify.services.Impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthenticationResponse> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(authenticationService.verifyOtp(email, otp));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        authenticationService.resendOtp(email);
        return ResponseEntity.ok("A new OTP has been sent to your email.");
    }

}

