package com.imad.eventify.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendInvitationEmail(String to, String eventName, String invitationLink) throws MessagingException;
    void sendRegistrationConfirmation(String to, String eventName, byte[] qrCode) throws MessagingException;
    void sendEmail(String to, String subject, String body) throws MessagingException; // optional
}
