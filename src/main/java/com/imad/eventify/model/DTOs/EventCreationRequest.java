package com.imad.eventify.model.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreationRequest {
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private boolean isPublic;
}
//لتبسيط إرسال البيانات عند إنشاء فعالية (بدون الحاجة لتمرير organizerId يدويًا، لأنه يؤخذ من المستخدم المسجل).