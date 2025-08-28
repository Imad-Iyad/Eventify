package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.AccessDeniedException;
import com.imad.eventify.model.DTOs.AttendanceConfirmationReqDTO;
import com.imad.eventify.model.DTOs.AttendanceConfirmationResDTO;
import com.imad.eventify.model.entities.AttendanceConfirmation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.repositories.AttendanceConfirmationRepository;
import com.imad.eventify.repositories.RegistrationRepository;
import com.imad.eventify.services.AttendanceConfirmationService;
import com.imad.eventify.services.UserService;
import com.imad.eventify.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceConfirmationServiceImpl implements AttendanceConfirmationService {

    private final AttendanceConfirmationRepository confirmationRepository;
    private final RegistrationRepository registrationRepository;
    private final UserService userService;

    @Override
    public AttendanceConfirmationResDTO confirmAttendance(AttendanceConfirmationReqDTO requestDTO) {
        // جلب المستخدم الحالي
        User currentUser = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);

        // التحقق من وجود التسجيل باستخدام الـ token
        Registration registration = registrationRepository.findByRegistrationToken(requestDTO.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        // التأكد أن المستخدم الحالي هو صاحب التسجيل
        if (!registration.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to confirm attendance for this registration");
        }

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
