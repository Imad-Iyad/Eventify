package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;

public interface InvitationService {
    InvitationResponseDTO sendInvitation(Event event, String email);
    Invitation getInvitationByToken(String token);
}


