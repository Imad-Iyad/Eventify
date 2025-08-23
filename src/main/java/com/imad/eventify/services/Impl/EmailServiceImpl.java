package com.imad.eventify.services.Impl;

import com.imad.eventify.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendInvitationEmail(String to, String eventName, String invitationLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("You're invited to: " + eventName);
        helper.setText(
                "<h2>You are invited to " + eventName + " 🎉</h2>" +
                        "<p>Click the link below to confirm your invitation:</p>" +
                        "<a href=\"" + invitationLink + "\">Confirm Invitation</a>",
                true
        );

        mailSender.send(message);
    }

    @Override
    public void sendRegistrationConfirmation(String to, String eventName, byte[] qrCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Registration confirmed: " + eventName);
        helper.setText(
                "<h2>Your registration for " + eventName + " is confirmed ✅</h2>" +
                        "<p>Scan the QR code below at the event entrance:</p>",
                true
        );

        helper.addAttachment("qrcode.png", () -> new ByteArrayInputStream(qrCode));
        mailSender.send(message);
    }

    @Override
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false);

        mailSender.send(message);
    }
}
