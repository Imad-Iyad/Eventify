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
public class UpdateEventDTO {
    @NotBlank(message = "Enter the valid Tittle")
    private String title;
    @NotBlank(message = "Enter the valid Description")
    private String description;
    @NotBlank(message = "Enter the valid StartDate")
    private LocalDateTime startDateTime;
    @NotBlank(message = "Enter the valid EndDate")
    private LocalDateTime endDateTime;
    @NotBlank(message = "Enter the valid Location")
    private String location;
    @NotBlank(message = "Enter the valid Type")
    private EventType eventType;
}
//To update Event information.
