package com.imad.eventify.security;

import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("eventSecurity")
@RequiredArgsConstructor
public class EventSecurity {

    private final EventRepository eventRepository;
    private final UserService userService;

    /**
     * Checks if the currently authenticated user is the organizer of the event.
     */
    public boolean isOrganizer(Long eventId) {
        var currentUser = userService.getCurrentUserEntity();
        return eventRepository.findById(eventId)
                .map(event -> event.getOrganizer().getId().equals(currentUser.getId()))
                .orElse(false);
    }
}
