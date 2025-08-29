package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.DTOs.RegistrationDTO;

public interface InvitationService {
    InvitationResponseDTO sendInvitation(Long eventId, String email);
    RegistrationDTO getInvitationByToken(String token);
}


