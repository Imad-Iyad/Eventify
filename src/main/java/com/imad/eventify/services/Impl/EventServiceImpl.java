package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.*;
import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventResponseDTO;
import com.imad.eventify.model.DTOs.UpdateEventDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.mappers.EventMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.repositories.RegistrationRepository;
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
    private final RegistrationRepository registrationRepository;

    @Override
    public EventResponseDTO createEvent(EventCreationRequest request) {
        User organizer = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(organizer);

        validateCreateEventRequest(request);

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .eventType(request.getEventType())
                .capacity(request.getCapacity())
                .organizer(organizer)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Event savedEvent = eventRepository.save(event);
        return toDtoUtil(savedEvent);
    }

    private void validateCreateEventRequest(EventCreationRequest request) {
        if (request.getStartDateTime() == null || request.getEndDateTime() == null) {
            throw new InvalidEventDateException("Start date and end date are required");
        }

        if (!request.getEndDateTime().isAfter(request.getStartDateTime())) {
            throw new InvalidEventDateException("End date must be after start date");
        }

        if (request.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidEventDateException("Start date must be in the future");
        }

        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new InvalidEventException("Capacity must be greater than 0");
        }

        if (request.getEventType() == null) {
            throw new InvalidEventException("Event type is required");
        }
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

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
    public EventResponseDTO updateEvent(Long eventId, UpdateEventDTO dto) {
        User currentUser = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this event");
        }

        LocalDateTime finalStart = dto.getStartDateTime() != null
                ? dto.getStartDateTime()
                : event.getStartDateTime();

        LocalDateTime finalEnd = dto.getEndDateTime() != null
                ? dto.getEndDateTime()
                : event.getEndDateTime();

        if (finalEnd.isBefore(finalStart)) {
            throw new InvalidEventDateException("End date must be after start date");
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            event.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            event.setDescription(dto.getDescription());
        }

        if (dto.getStartDateTime() != null) {
            event.setStartDateTime(dto.getStartDateTime());
        }

        if (dto.getEndDateTime() != null) {
            event.setEndDateTime(dto.getEndDateTime());
        }

        if (dto.getLocation() != null && !dto.getLocation().isBlank()) {
            event.setLocation(dto.getLocation());
        }

        event.setUpdatedAt(LocalDateTime.now());

        Event saved = eventRepository.save(event);
        return eventMapper.toDTO(saved);
    }

    @Override
    public void deleteEvent(Long eventId) {
        User currentUser = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this event");
        }

        if (registrationRepository.existsByEvent(event)) {
            throw new EventDeletionNotAllowedException("Cannot delete event because it has registrations");
        }

        eventRepository.delete(event);
    }

    public EventResponseDTO toDtoUtil(Event event) {
        EventResponseDTO dto = eventMapper.toDTO(event);
        dto.setOrganizerId(event.getOrganizer().getId());
        return dto;
    }
}

