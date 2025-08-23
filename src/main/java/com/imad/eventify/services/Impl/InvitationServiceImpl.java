package com.imad.eventify.services.Impl;

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
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InvitationMapper invitationMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public InvitationResponseDTO sendInvitation(Long eventId, String email) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));
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
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invitation email", e);
        }

        return invitationMapper.toResponseDTO(invitation);
    }

    @Override
    public RegistrationDTO getInvitationByToken(String token, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation token"));

        return RegistrationDTO.builder()
                .userId(user.getId())
                .eventId(invitation.getEvent().getId())
                .invitationId(invitation.getId())
                .inviteeEmail(invitation.getEmail())
                .attendanceConfirmed(false)
                .build();
    }
}
