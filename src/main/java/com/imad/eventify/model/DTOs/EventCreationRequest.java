package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.EventType;
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
    private String location;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private EventType eventType;
    private Integer capacity;
    private Long OrganizerId;
}
//لتبسيط إرسال البيانات عند إنشاء فعالية (بدون الحاجة لتمرير organizerId يدويًا، لأنه يؤخذ من المستخدم المسجل).