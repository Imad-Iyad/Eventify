package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Endpoint to register a user for an event.
     * This handles both public registrations and private ones via invitation.
     *
     * @param registrationDTO DTO containing userId, eventId, and optionally invitationId.
     * @return The created Registration details.
     */
    @PostMapping
    public ResponseEntity<RegistrationDTO> registerForEvent(@RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO createdRegistration = registrationService.registerToEvent(registrationDTO);
        return new ResponseEntity<>(createdRegistration, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get registration details using the unique registration token.
     * This can be used to verify a ticket/QR code.
     *
     * @param token The unique token generated during registration.
     * @return The registration details.
     */
    @GetMapping("/{token}")
    public ResponseEntity<RegistrationDTO> getRegistrationByToken(@PathVariable String token) {
        RegistrationDTO registrationDTO = registrationService.getRegistrationByToken(token);
        return ResponseEntity.ok(registrationDTO);
    }
}