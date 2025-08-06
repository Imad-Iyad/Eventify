package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.InvitationDTO;

import java.util.List;

public interface InvitationService {
    InvitationDTO sendInvitation(InvitationDTO invitationDTO);
    List<InvitationDTO> getInvitationsByEventId(Long eventId);
    void cancelInvitation(Long invitationId);
}

