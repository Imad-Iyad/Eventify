package com.imad.eventify.services.Impl;

import com.imad.eventify.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from}")
    private String from;

    private static final String RESEND_URL = "https://api.resend.com/emails";

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void sendInvitationEmail(String to, String eventName, String invitationLink) {
        String html = """
            <h2>You are invited to %s 🎉</h2>
            <p>Click the link below to confirm your invitation:</p>
            <a href="%s">Confirm Invitation</a>
            """.formatted(eventName, invitationLink);

        sendEmail(to, "You're invited to: " + eventName, html, "You are invited to " + eventName + ". Link: " + invitationLink);
    }

    @Override
    public void sendRegistrationConfirmation(String to, String eventName, byte[] qrCode) {
        String qrB64 = java.util.Base64.getEncoder().encodeToString(qrCode);
        String html = """
            <h2>Your registration for %s is confirmed ✅</h2>
            <p>Scan the QR code below at the event entrance:</p>
            <img src="data:image/png;base64,%s" />
            """.formatted(eventName, qrB64);
        sendEmail(to, "Registration confirmed: " + eventName, html, "Registration confirmed for " + eventName);
    }

    @Override
    public void sendEmail(String to, String subject, String bodyTextOnly) {
        sendEmail(to, subject, "<p>" + bodyTextOnly + "</p>", bodyTextOnly);
    }

    private void sendEmail(String to, String subject, String html, String textFallback) {
        // الجسم كـ Map لتفادي مشاكل الهروب/الاقتباسات
        Map<String, Object> payload = new HashMap<>();
        payload.put("from", from);                 // يجب أن يكون على دومين مُفعّل في Resend
        payload.put("to", List.of(to));
        payload.put("subject", subject);
        payload.put("html", html);
        payload.put("text", textFallback);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);             // Authorization: Bearer re_...
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> res = restTemplate.exchange(RESEND_URL, HttpMethod.POST, entity, String.class);
            System.out.println("Resend status=" + res.getStatusCode().value());
            System.out.println("Resend body=" + res.getBody());

            if (res.getStatusCode().is2xxSuccessful()) {
                String id = extractId(res.getBody());
                System.out.println("Resend messageId=" + id);
            } else {
                log.info("Failed to send email");
            }
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    // استخراج سريع لل id
    private String extractId(String body) {
        if (body == null) return null;
        int i = body.indexOf("\"id\"");
        if (i < 0) return null;
        int c = body.indexOf(":", i);
        int q1 = body.indexOf('"', c);
        int q2 = body.indexOf('"', q1 + 1);
        if (q1 < 0 || q2 < 0) return null;
        return body.substring(q1 + 1, q2);
    }
}