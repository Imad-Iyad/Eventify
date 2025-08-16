package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventResponseDTO;
import com.imad.eventify.model.DTOs.UpdateEventDTO;

import java.util.List;

public interface EventService {
    EventResponseDTO createEvent(EventCreationRequest request);
    EventResponseDTO getEventById(Long id);
    List<EventResponseDTO> getAllEvents();
    EventResponseDTO updateEvent(Long id, UpdateEventDTO UpdateEventDTO);
    void deleteEvent(Long id);
}

