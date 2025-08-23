package com.imad.eventify.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private Long userId;           // ID المستخدم
    private Long eventId;          // ID الحدث
    private Long invitationId;     // ID الدعوة (اختياري، إذا جاي من دعوة)
    private String inviteeEmail;   // البريد الإلكتروني من الدعوة
}
