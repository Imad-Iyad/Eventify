package com.imad.eventify.services.Impl;

import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.enums.InvitationStatus;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.services.EmailService;
import com.imad.eventify.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    //private final InvitationMapper invitationMapper;

    @Override
    public Invitation sendInvitation(Event event, String email) {
        // إنشاء الدعوة
        Invitation invitation = Invitation.builder()
                .event(event)
                .email(email)
                .status(InvitationStatus.PENDING)
                .token(UUID.randomUUID().toString())
                .sentAt(LocalDateTime.now())
                .build();

        // حفظ الدعوة
        invitationRepository.save(invitation);

        // بناء رابط الدعوة
        String link = "https://eventify.com/invite?token=" + invitation.getToken();

        // إرسال الإيميل
        String subject = "Invitation to: " + event.getTitle();
        String body = "Hello,\n\nYou have been invited to the event: " + event.getTitle() +
                "\nDate: " + event.getStartDateTime() +
                "\nLocation: " + event.getLocation() +
                "\n\nClick here to register: " + link +
                "\n\nNote: This link can only be used once.";

        emailService.sendEmail(email, subject, body);

        return invitation;
    }

    @Override
    public Invitation getInvitationByToken(String token) {
        return invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation token"));
    }
}
