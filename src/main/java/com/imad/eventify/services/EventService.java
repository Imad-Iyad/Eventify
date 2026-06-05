package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventResponseDTO;
import com.imad.eventify.model.DTOs.MyEventResponseDTO;
import com.imad.eventify.model.DTOs.UpdateEventDTO;
import com.imad.eventify.model.entities.enums.EventType;

import java.util.List;

public interface EventService {
    EventResponseDTO createEvent(EventCreationRequest request);
    EventResponseDTO getEventById(Long id);
    List<EventResponseDTO> getAllEvents();
    List<MyEventResponseDTO> getCurrentUserRegisteredEventsByType(EventType eventType);
    EventResponseDTO updateEvent(Long id, UpdateEventDTO UpdateEventDTO);
    void deleteEvent(Long id);
}
