package com.imad.eventify.repositories;

import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.entities.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByUserAndEvent(User user, Event event);
    boolean existsByInvitation(Invitation invitation);
    Optional<Registration> findByRegistrationToken(String token);
    boolean existsByEvent(Event event);
    @Query("select distinct r.event from Registration r where r.user = :user and r.event.eventType = :eventType")
    List<Event> findRegisteredEventsByUserAndEventType(@Param("user") User user, @Param("eventType") EventType eventType);
    @Query("select r from Registration r join fetch r.event where r.user = :user and r.event.eventType = :eventType")
    List<Registration> findByUserAndEventTypeWithEvent(@Param("user") User user, @Param("eventType") EventType eventType);
}
