package com.imad.eventify.services.Impl;

import com.imad.eventify.model.DTOs.AttendanceConfirmationReqDTO;
import com.imad.eventify.model.DTOs.AttendanceConfirmationResDTO;
import com.imad.eventify.model.entities.AttendanceConfirmation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.repositories.AttendanceConfirmationRepository;
import com.imad.eventify.repositories.RegistrationRepository;
import com.imad.eventify.services.AttendanceConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceConfirmationServiceImpl implements AttendanceConfirmationService {

    private final AttendanceConfirmationRepository confirmationRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public AttendanceConfirmationResDTO confirmAttendance(AttendanceConfirmationReqDTO requestDTO) {
        // Verify registration from token
        Registration registration = registrationRepository.findByRegistrationToken(requestDTO.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        // Check if already confirmed
        confirmationRepository.findByRegistration(registration).ifPresent(c -> {
            throw new RuntimeException("Attendance already confirmed");
        });

        // Save confirmation
        AttendanceConfirmation confirmation = AttendanceConfirmation.builder()
                .registration(registration)
                .user(registration.getUser())
                .event(registration.getEvent())
                .confirmedAt(LocalDateTime.now())
                .build();

        confirmationRepository.save(confirmation);

        // Return DTO
        return AttendanceConfirmationResDTO.builder()
                .id(confirmation.getId())
                .userId(confirmation.getUser().getId())
                .eventId(confirmation.getEvent().getId())
                .registrationId(confirmation.getRegistration().getId())
                .confirmedAt(confirmation.getConfirmedAt())
                .build();
    }
}
