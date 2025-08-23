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
public class RegistrationResDTO {
    private Long id;               // للتسجيل النهائي (بعد الحفظ)
    private Long userId;           // ID المستخدم
    private Long eventId;          // ID الحدث
    private Long invitationId;     // ID الدعوة (اختياري، إذا جاي من دعوة)
    private String inviteeEmail;   // البريد الإلكتروني من الدعوة
    private String registrationToken; // Token للتسجيل النهائي (للتحقق / QR code)
    private LocalDateTime registeredAt; // وقت التسجيل
}