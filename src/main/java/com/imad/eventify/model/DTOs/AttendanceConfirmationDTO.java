package com.imad.eventify.model.DTOs;

import lombok.Data;

@Data
public class AttendanceConfirmationDTO {
    private String token; // أو يمكن استخدام QR code hash
}
//يستخدم لتأكيد الحضور عبر رابط أو مسح QR.
