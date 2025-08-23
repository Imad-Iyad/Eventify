package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.EventNotFoundException;
import com.imad.eventify.Exceptions.UserNotFoundException;
import com.imad.eventify.model.DTOs.RegistrationResDTO;
import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.entities.enums.InvitationStatus;
import com.imad.eventify.model.mappers.RegistrationMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.repositories.RegistrationRepository;
import com.imad.eventify.repositories.UserRepository;
import com.imad.eventify.services.EmailService;
import com.imad.eventify.services.RegistrationService;
import com.imad.eventify.utils.QRCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final InvitationRepository invitationRepository;
    private final RegistrationMapper registrationMapper;
    private final EmailService emailService;

    // for both private and public events
    @Override
    @Transactional
    public RegistrationResDTO registerToEvent(RegistrationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.getUserId()));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + dto.getEventId()));

        // Prevention of refined registration
        boolean alreadyRegistered = registrationRepository.existsByUserAndEvent(user, event);
        if (alreadyRegistered) {
            throw new RuntimeException("User is already registered for this event");
        }

        Invitation invitation = null;
        if (dto.getInvitationId() != null) {
            invitation = invitationRepository.findById(dto.getInvitationId())
                    .orElseThrow(() -> new RuntimeException("Invitation not found"));

            // Verification if invitation is used
            if (invitation.getStatus() == InvitationStatus.USED) {
                throw new RuntimeException("This invitation has already been used");
            }

            // Check if it is used by registering
            boolean invitationUsed = registrationRepository.existsByInvitation(invitation);
            if (invitationUsed) {
                throw new RuntimeException("This invitation has already been used");
            }
        }

        // The generation of Token and QR
        String token = UUID.randomUUID().toString();
        byte[] qrCode = QRCodeGenerator.generateQRCode(token, 250, 250);

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .registrationToken(token)
                .qrCode(qrCode)
                .invitation(invitation)
                .registeredAt(LocalDateTime.now())
                .build();

        Registration saved = registrationRepository.save(registration);

        // Update the status of invitation
        if (invitation != null) {
            invitation.setStatus(InvitationStatus.USED);
            invitation.setUsedAt(LocalDateTime.now());
            invitationRepository.save(invitation);
        }

        //  Send the registration confirmation with QR
        try {
            emailService.sendRegistrationConfirmation(user.getEmail(), event.getTitle(), qrCode);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send registration confirmation email", e);
        }

        RegistrationResDTO registrationResDTO = registrationMapper.toDTO(saved);
        registrationResDTO.setInviteeEmail(user.getEmail());
        return registrationResDTO;
    }

    @Override
    @Transactional
    public RegistrationResDTO getRegistrationByToken(String token) {
        Registration registration = registrationRepository.findByRegistrationToken(token)
                .orElseThrow(() -> new RuntimeException("Registration not found with token: " + token));
        return registrationMapper.toDTO(registration);
    }

}
