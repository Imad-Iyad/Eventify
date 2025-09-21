package com.imad.eventify.repositories;

import com.imad.eventify.model.entities.EmailOtp;
import com.imad.eventify.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findByUserAndOtpCodeAndUsedFalse(User user, String otpCode);
    void deleteAllByUser(User user);
}