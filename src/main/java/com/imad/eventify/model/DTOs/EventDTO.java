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
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private boolean isPublic;
    private Long organizerId;
}
//لإنشاء وتحديث معلومات الفعالية.