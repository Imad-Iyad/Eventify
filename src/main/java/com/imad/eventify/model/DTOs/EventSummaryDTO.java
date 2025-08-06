package com.imad.eventify.model.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSummaryDTO {
    private Long id;
    private String title;
    private LocalDateTime dateTime;
    private boolean isPublic;
}
//للعرض السريع لفعاليات المستخدم (بدون التفاصيل الثقيلة).