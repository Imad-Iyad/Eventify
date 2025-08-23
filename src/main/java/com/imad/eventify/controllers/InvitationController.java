package com.imad.eventify.controllers;

import com.imad.eventify.Exceptions.EventNotFoundException;
import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final EventRepository eventRepository;

    // /api/v1/invitations/send?eventId=1&email=test@mail.com
    @PostMapping("/send")
    public ResponseEntity<InvitationResponseDTO> sendInvitation(@RequestParam Long eventId, @RequestParam String email) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        InvitationResponseDTO invitationResponseDTO = invitationService.sendInvitation(event, email);
        return ResponseEntity.ok(invitationResponseDTO);
    }

    // /api/v1/invitations/b8b05a9c-54d9-45c7-bf57-02aef2286db9
    @GetMapping("/{token}")
    public ResponseEntity<Invitation> getInvitation(@PathVariable String token) {
        Invitation invitation = invitationService.getInvitationByToken(token);
        return ResponseEntity.ok(invitation);
    }
}
