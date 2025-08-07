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
public class EventSummaryDTO {
    private Long id;
    private String title;
    private LocalDateTime dateTime;
    private boolean isPublic;
}
//للعرض السريع لفعاليات المستخدم (بدون التفاصيل الثقيلة).