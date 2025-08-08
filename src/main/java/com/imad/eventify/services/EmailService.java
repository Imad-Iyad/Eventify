package com.imad.eventify.services;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}

