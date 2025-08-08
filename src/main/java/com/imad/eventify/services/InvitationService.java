package com.imad.eventify.services;

import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;

public interface InvitationService {
    Invitation sendInvitation(Event event, String email);
    Invitation getInvitationByToken(String token);
}


