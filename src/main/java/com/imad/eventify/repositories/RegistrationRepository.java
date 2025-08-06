package com.imad.eventify.repositories;

import com.imad.eventify.model.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// RegistrationRepository.java
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByEventIdAndUserId(Long eventId, Long userId);
    List<Registration> findByEventId(Long eventId);
    Registration findByRegistrationToken(String token);
}
