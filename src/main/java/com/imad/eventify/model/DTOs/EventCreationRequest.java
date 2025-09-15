package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.EventType;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Enter the Tittle")
    private String title;
    @NotBlank(message = "Enter the Description")
    private String description;
    @NotBlank(message = "Enter the Location")
    private String location;
    @NotBlank(message = "Enter the Start Date Time")
    private LocalDateTime startDateTime;
    @NotBlank(message = "Enter the End Date Time")
    private LocalDateTime endDateTime;
    @NotBlank(message = "Enter the Event Type")
    private EventType eventType;
    @NotBlank(message = "Enter the Capacity")
    private Integer capacity;
}
//لتبسيط إرسال البيانات عند إنشاء فعالية (بدون الحاجة لتمرير organizerId يدويًا، لأنه يؤخذ من المستخدم المسجل-->authenticated user).