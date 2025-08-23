package com.imad.eventify.services.Impl;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.enums.InvitationStatus;
import com.imad.eventify.model.mappers.InvitationMapper;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.services.EmailService;
import com.imad.eventify.services.InvitationService;
import jakarta.mail.MessagingException;
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
    private final InvitationMapper invitationMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public InvitationResponseDTO sendInvitation(Event event, String email) {
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
    public Invitation getInvitationByToken(String token) {
        return invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation token"));
    }
}
