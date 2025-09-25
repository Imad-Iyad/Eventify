package com.imad.eventify.services;

public interface EmailService {
    void sendInvitationEmail(String to, String eventName, String invitationLink);
    void sendRegistrationConfirmation(String to, String eventName, byte[] qrCode);
    void sendEmail(String to, String subject, String body);
}
