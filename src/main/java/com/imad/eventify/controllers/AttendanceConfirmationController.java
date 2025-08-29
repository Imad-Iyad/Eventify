package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.AttendanceConfirmationReqDTO;
import com.imad.eventify.model.DTOs.AttendanceConfirmationResDTO;
import com.imad.eventify.services.AttendanceConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceConfirmationController {

    private final AttendanceConfirmationService confirmationService;

    /**
     * Endpoint to confirm attendance via QR Code (token).
     *
     * @param requestDTO DTO containing the registration token (from QR code).
     * @return Attendance confirmation details.
     */
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ADMIN')")
    @PostMapping("/confirm")
    public ResponseEntity<AttendanceConfirmationResDTO> confirmAttendance(
            @RequestBody AttendanceConfirmationReqDTO requestDTO) {
        return ResponseEntity.ok(confirmationService.confirmAttendance(requestDTO));
    }
}