package com.imad.eventify.services.Impl;

import com.imad.eventify.model.DTOs.InvitationDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.enums.InvitationStatus;
import com.imad.eventify.model.mappers.InvitationMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final InvitationMapper invitationMapper;

    @Override
    public InvitationDTO sendInvitation(InvitationDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + dto.getEventId()));

        Invitation invitation = invitationMapper.toEntity(dto);
        invitation.setEvent(event);
        invitation.setStatus(InvitationStatus.PENDING);
        Invitation saved = invitationRepository.save(invitation);
        return invitationMapper.toDTO(saved);
    }

    @Override
    public List<InvitationDTO> getInvitationsByEventId(Long eventId) {
        List<Invitation> invitations = invitationRepository.findByEventId(eventId);
        return invitations.stream()
                .map(invitationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setStatus(InvitationStatus.CANCELLED);
        invitationRepository.save(invitation);
    }
}

