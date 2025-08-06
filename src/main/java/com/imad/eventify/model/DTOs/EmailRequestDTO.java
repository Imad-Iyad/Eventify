package com.imad.eventify.model.DTOs;

import lombok.Data;

@Data
public class EmailRequestDTO {
    private String to;
    private String subject;
    private String body;
}
//لإرسال الإيميل (سواء رابط تأكيد أو QR).
