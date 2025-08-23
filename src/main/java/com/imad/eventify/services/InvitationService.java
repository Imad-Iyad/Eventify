package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.DTOs.RegistrationDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface InvitationService {
    InvitationResponseDTO sendInvitation(Long eventId, String email);
    RegistrationDTO getInvitationByToken(String token, UserDetails userDetails);
}


