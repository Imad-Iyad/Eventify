package com.imad.eventify.repositories;

import com.imad.eventify.model.entities.AttendanceConfirmation;
import com.imad.eventify.model.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceConfirmationRepository extends JpaRepository<AttendanceConfirmation, Long> {
    Optional<AttendanceConfirmation> findByRegistrationId(Long registrationId);
    Optional<AttendanceConfirmation> findByRegistration(Registration registration);
}

