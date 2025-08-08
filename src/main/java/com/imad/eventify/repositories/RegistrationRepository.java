package com.imad.eventify.repositories;

import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByUserAndEvent(User user, Event event);
    boolean existsByInvitation(Invitation invitation);
    Optional<Registration> findByRegistrationToken(String token);
    Optional<Registration> findByInvitationId(Long invitationId);
    Optional<Registration> findByEventIdAndUserId(Long eventId, Long userId);
    List<Registration> findByEventId(Long eventId);
}
