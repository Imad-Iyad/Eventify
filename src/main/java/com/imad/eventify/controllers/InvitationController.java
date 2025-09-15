package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.services.InvitationService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
@Validated
public class InvitationController {

    private final InvitationService invitationService;

    // /api/v1/invitations/send?eventId=1&email=test@mail.com
    // Only the organizer of the event or ADMIN can send invitations
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOrganizer(#eventId)")
    @PostMapping("/send")
    public ResponseEntity<InvitationResponseDTO> sendInvitation(
            @RequestParam @NotNull(message = "ID cannot be null") @Min(value = 1, message = "ID must be positive") long eventId,
            @RequestParam @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        InvitationResponseDTO invitationResponseDTO = invitationService.sendInvitation(eventId, email);
        return ResponseEntity.ok(invitationResponseDTO);
    }
}
