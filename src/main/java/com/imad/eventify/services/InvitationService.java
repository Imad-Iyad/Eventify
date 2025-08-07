package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.InvitationDTO;
import com.imad.eventify.model.entities.enums.InvitationStatus;

import java.util.List;

public interface InvitationService {
    InvitationDTO createInvitation(InvitationDTO invitationDTO);
    InvitationDTO respondToInvitation(Long invitationId, InvitationStatus status);
    InvitationDTO getInvitationById(Long id);
    List<InvitationDTO> getInvitationsByEventId(Long eventId);
    void deleteInvitation(Long id);
}


