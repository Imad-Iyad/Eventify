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
public class AttendanceConfirmationResDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private Long registrationId;
    private LocalDateTime confirmedAt;
}