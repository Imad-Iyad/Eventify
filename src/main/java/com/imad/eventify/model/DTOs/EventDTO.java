package com.imad.eventify.model.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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