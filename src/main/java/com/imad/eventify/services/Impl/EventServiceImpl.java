package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.AccessDeniedException;
import com.imad.eventify.Exceptions.EventNotFoundException;
import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventResponseDTO;
import com.imad.eventify.model.DTOs.UpdateEventDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.mappers.EventMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.services.EventService;
import com.imad.eventify.services.UserService;
import com.imad.eventify.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponseDTO createEvent(EventCreationRequest request) {
        User organizer = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(organizer);

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .eventType(request.getEventType())
                .capacity(request.getCapacity())
                .organizer(organizer) // Connecting the organizer to the Event
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Event savedEvent = eventRepository.save(event);
        return toDtoUtil(savedEvent);
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return toDtoUtil(event);
    }

    @Override
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::toDtoUtil)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDTO updateEvent(Long id, UpdateEventDTO updateEventDTO) {
        User currentUser = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);

        Event existing =  eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        if (!existing.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this event");
        }
        existing.setTitle(updateEventDTO.getTitle());
        existing.setDescription(updateEventDTO.getDescription());
        existing.setLocation(updateEventDTO.getLocation());
        existing.setStartDateTime(updateEventDTO.getStartDateTime());
        existing.setEndDateTime(updateEventDTO.getEndDateTime());
        existing.setEventType(updateEventDTO.getEventType());
        existing.setUpdatedAt(LocalDateTime.now());
        EventResponseDTO dto = eventMapper.toDTO(eventRepository.save(existing));
        dto.setOrganizer(existing.getOrganizer());
        return dto;
    }

    @Override
    public void deleteEvent(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);

        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        if (!existing.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this event");
        }
        eventRepository.deleteById(id);
    }

    public EventResponseDTO toDtoUtil(Event event) {
        EventResponseDTO dto = eventMapper.toDTO(event);
        dto.setOrganizer(event.getOrganizer());
        return dto;
    }
}

