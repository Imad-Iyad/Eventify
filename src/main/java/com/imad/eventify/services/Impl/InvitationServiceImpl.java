package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.AccessDeniedException;
import com.imad.eventify.Exceptions.EventNotFoundException;
import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.entities.enums.InvitationStatus;
import com.imad.eventify.model.mappers.InvitationMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.repositories.UserRepository;
import com.imad.eventify.services.EmailService;
import com.imad.eventify.services.InvitationService;
import com.imad.eventify.services.UserService;
import com.imad.eventify.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InvitationMapper invitationMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public InvitationResponseDTO sendInvitation(Long eventId, String email) {
        User currentUser = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to invite users to this event");
        }

        Invitation invitation = Invitation.builder()
                .event(event)
                .email(email)
                .status(InvitationStatus.PENDING)
                .token(UUID.randomUUID().toString())
                .sentAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);

        String link = baseUrl + "/api/v1/registrations/by-invitation/" + invitation.getToken();

        // HTML
        try {
            emailService.sendInvitationEmail(email, event.getTitle(), link);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send invitation email", e);
        }

        return invitationMapper.toResponseDTO(invitation);
    }

    @Override
    public RegistrationDTO getInvitationByToken(String token) {
        User user = userService.getCurrentUserEntity();
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation token"));

        UserValidator.assertUserIsActive(user);

        // Security check: only the invitee can use this token
        if (!invitation.getEmail().equals(user.getEmail())) {
            throw new AccessDeniedException("You are not allowed to access this invitation");
        }

        return RegistrationDTO.builder()
                .eventId(invitation.getEvent().getId())
                .invitationId(invitation.getId())
                .build();
    }
}
