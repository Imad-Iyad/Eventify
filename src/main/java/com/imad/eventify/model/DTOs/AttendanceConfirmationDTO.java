package com.imad.eventify.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceConfirmationDTO {
    private String token; // أو يمكن استخدام QR code hash
}
//يستخدم لتأكيد الحضور عبر رابط أو مسح QR.
