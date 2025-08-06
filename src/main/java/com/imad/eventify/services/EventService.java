package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.EventDTO;
import com.imad.eventify.model.DTOs.EventCreationRequest;

import java.util.List;

public interface EventService {
    EventDTO createEvent(EventCreationRequest request);
    EventDTO getEventById(Long id);
    List<EventDTO> getAllEvents();
    void deleteEvent(Long id);
}

