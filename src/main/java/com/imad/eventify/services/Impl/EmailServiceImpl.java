package com.imad.eventify.services.Impl;

import com.imad.eventify.services.EmailService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailServiceImpl implements EmailService {

    //@Value("${resend.api.key}")
    private final  String apiKey = "re_NTdTvbBH_BSmXBHDNoaPz1DrLWB9BM8LK"; // API Key from Resend

    private static final String RESEND_URL = "https://api.resend.com/v1/emails";

    @Override
    public void sendInvitationEmail(String to, String eventName, String invitationLink) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String htmlContent = "<h2>You are invited to " + eventName + " 🎉</h2>" +
                "<p>Click the link below to confirm your invitation:</p>" +
                "<a href=\"" + invitationLink + "\">Confirm Invitation</a>";

        String emailJson = "{\n" +
                "\"from\": \"noreply@imadapps.com\",\n" +
                "\"to\": [\"" + to + "\"],\n" +
                "\"subject\": \"You're invited to: " + eventName + "\",\n" +
                "\"html\": \"" + htmlContent + "\"\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(emailJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(RESEND_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // إذا كانت الاستجابة ناجحة (رمز حالة 2xx)
                System.out.println("Email sent successfully.");
            } else {
                // إذا كانت الاستجابة فاشلة
                System.err.println("Failed to send email: " + response.getBody());
                // هنا يمكن إضافة بعض إجراءات المعالجة مثل إرسال تنبيه أو تسجيل الخطأ
            }
        } catch (Exception e) {
            // في حالة حدوث خطأ أثناء إرسال الطلب
            System.err.println("Error sending email: " + e.getMessage());
            // هنا يمكن إضافة إجراءات لإعادة المحاولة أو إرسال تنبيه
        }
    }

    @Override
    public void sendRegistrationConfirmation(String to, String eventName, byte[] qrCode) {
        String qrCodeBase64 = encodeToBase64(qrCode); // تحويل QR Code إلى Base64

        String htmlContent = "<h2>Your registration for " + eventName + " is confirmed ✅</h2>" +
                "<p>Scan the QR code below at the event entrance:</p>" +
                "<img src=\"data:image/png;base64," + qrCodeBase64 + "\" />";

        String emailJson = "{\n" +
                "\"from\": \"noreply@imadapps.com\",\n" +
                "\"to\": [\"" + to + "\"],\n" +
                "\"subject\": \"Registration confirmed: " + eventName + "\",\n" +
                "\"html\": \"" + htmlContent + "\"\n" +
                "}";

        sendEmailWithResendApi(emailJson);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        String emailJson = "{\n" +
                "\"from\": \"noreply@imadapps.com\",\n" +
                "\"to\": [\"" + to + "\"],\n" +
                "\"subject\": \"" + subject + "\",\n" +
                "\"text\": \"" + body + "\"\n" +
                "}";

        sendEmailWithResendApi(emailJson);
    }

    private void sendEmailWithResendApi(String emailJson) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(emailJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(RESEND_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Email sent successfully.");
            } else {
                System.err.println("Failed to send email: " + response.getBody());
                // إجراءات لمعالجة الخطأ مثل إعادة المحاولة أو إرسال تنبيه
            }
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            // معالجة الأخطاء مثل إعادة المحاولة أو إرسال تنبيه
        }
    }

    private String encodeToBase64(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }
}