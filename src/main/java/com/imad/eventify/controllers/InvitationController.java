package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    // /api/v1/invitations/send?eventId=1&email=test@mail.com
    // Only the organizer of the event or ADMIN can send invitations
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOrganizer(#eventId)")
    @PostMapping("/send")
    public ResponseEntity<InvitationResponseDTO> sendInvitation(@RequestParam Long eventId, @RequestParam String email) {
        InvitationResponseDTO invitationResponseDTO = invitationService.sendInvitation(eventId, email);
        return ResponseEntity.ok(invitationResponseDTO);
    }
}
