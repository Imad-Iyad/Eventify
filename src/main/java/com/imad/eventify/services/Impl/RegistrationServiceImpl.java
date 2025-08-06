package com.imad.eventify.services.Impl;

import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.mappers.RegistrationMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.repositories.RegistrationRepository;
import com.imad.eventify.repositories.UserRepository;
import com.imad.eventify.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final RegistrationMapper registrationMapper;

    @Override
    public RegistrationDTO registerToEvent(RegistrationDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Invitation invitation = invitationRepository.findById(dto.getInvitationId())
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        Registration registration = registrationMapper.toEntity(dto);
        registration.setEvent(event);
        registration.setUser(user);
        registration.setInvitation(invitation);
        registration.setRegistrationToken(UUID.randomUUID().toString());

        Registration saved = registrationRepository.save(registration);
        return registrationMapper.toDTO(saved);
    }

    @Override
    public RegistrationDTO getRegistrationByToken(String token) {
        Registration registration = registrationRepository.findByRegistrationToken(token)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        return registrationMapper.toDTO(registration);
    }
}

