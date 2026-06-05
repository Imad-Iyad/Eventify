package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.DTOs.RegistrationDTO;

import java.util.List;

public interface InvitationService {
    InvitationResponseDTO sendInvitation(Long eventId, String email);
    RegistrationDTO getInvitationByToken(String token);
    List<InvitationResponseDTO> getMyInvitations();
}


