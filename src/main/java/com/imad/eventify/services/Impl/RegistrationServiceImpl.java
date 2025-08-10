package com.imad.eventify.services.Impl;

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
import com.imad.eventify.services.RegistrationService;
import com.imad.eventify.utils.QRCodeGenerator;
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

    // for both private and public events
    @Override
    public RegistrationDTO registerToEvent(RegistrationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // منع التسجيل المكرر
        boolean alreadyRegistered = registrationRepository.existsByUserAndEvent(user, event);
        if (alreadyRegistered) {
            throw new RuntimeException("User is already registered for this event");
        }

        Invitation invitation = null;
        if (dto.getInvitationId() != null) {
            invitation = invitationRepository.findById(dto.getInvitationId())
                    .orElseThrow(() -> new RuntimeException("Invitation not found"));

            // التحقق إذا الدعوة مستخدمة
            if (invitation.getStatus() == InvitationStatus.USED) {
                throw new RuntimeException("This invitation has already been used");
            }

            // تحقق إذا تم استخدامها عبر التسجيل
            boolean invitationUsed = registrationRepository.existsByInvitation(invitation);
            if (invitationUsed) {
                throw new RuntimeException("This invitation has already been used");
            }
        }

        // توليد توكن و QR
        String token = UUID.randomUUID().toString();
        byte[] qrCode = QRCodeGenerator.generateQRCode(token, 250, 250);

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .registrationToken(token)
                .qrCode(qrCode)
                .invitation(invitation)
                .registeredAt(LocalDateTime.now())
                .attendanceConfirmed(false)
                .build();

        Registration saved = registrationRepository.save(registration);

        // إذا فيه دعوة، نحدث حالتها
        if (invitation != null) {
            invitation.setStatus(InvitationStatus.USED);
            invitation.setUsedAt(LocalDateTime.now());
            invitationRepository.save(invitation);
        }

        return registrationMapper.toDTO(saved);
    }

    @Override
    public RegistrationDTO getRegistrationByToken(String token) {
        Registration registration = registrationRepository.findByRegistrationToken(token)
                .orElseThrow(() -> new RuntimeException("Registration not found with token: " + token));
        return registrationMapper.toDTO(registration);
    }
}
