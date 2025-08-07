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
public class RegistrationDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private Long invitationId; // Nullable إذا التسجيل عام
    private String registrationToken;
    private byte[] qrCode;
    private LocalDateTime registeredAt;
    private Boolean attendanceConfirmed;
}