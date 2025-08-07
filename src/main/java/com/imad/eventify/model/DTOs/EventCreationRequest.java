package com.imad.eventify.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreationRequest {
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private boolean isPublic;
}
//لتبسيط إرسال البيانات عند إنشاء فعالية (بدون الحاجة لتمرير organizerId يدويًا، لأنه يؤخذ من المستخدم المسجل).