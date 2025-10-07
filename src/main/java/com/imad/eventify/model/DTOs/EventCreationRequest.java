package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Enter the Start Date Time")
    private LocalDateTime startDateTime;

    @NotNull(message = "Enter the End Date Time")
    private LocalDateTime endDateTime;

    @NotNull(message = "Enter the Event Type")
    private EventType eventType;

    @NotNull(message = "Enter the Capacity")
    private Integer capacity;
}
//لتبسيط إرسال البيانات عند إنشاء فعالية (بدون الحاجة لتمرير organizerId يدويًا، لأنه يؤخذ من المستخدم المسجل-->authenticated user).