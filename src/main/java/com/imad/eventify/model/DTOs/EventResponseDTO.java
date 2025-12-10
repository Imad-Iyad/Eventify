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
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private EventType eventType;
}
