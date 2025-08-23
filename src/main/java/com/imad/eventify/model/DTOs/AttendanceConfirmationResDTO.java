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
    private Long registrationId;
    private Long eventId;
    private String attendeeName;
    private LocalDateTime confirmedAt;
    private String message; // مثلا "Attendance confirmed successfully"
}
