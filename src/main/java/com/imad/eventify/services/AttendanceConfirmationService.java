package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.AttendanceConfirmationReqDTO;
import com.imad.eventify.model.DTOs.AttendanceConfirmationResDTO;

public interface AttendanceConfirmationService {
    AttendanceConfirmationResDTO confirmAttendance(AttendanceConfirmationReqDTO requestDTO);
}
