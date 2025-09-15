package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventResponseDTO;
import com.imad.eventify.model.DTOs.UpdateEventDTO;
import com.imad.eventify.services.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(
            @PathVariable("id") @NotNull(message = "ID cannot be null") @Min(value = 1, message = "ID must be positive") long id) {
        EventResponseDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    // Any Organizer Can Create new event
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')") // Only Organizers and Admins Can Create Events
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventCreationRequest eventDTO) {
        EventResponseDTO createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(201).body(createdEvent);
    }

    // Only The Organizer Who Have The Event Can Update it
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOrganizer(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable("id") @NotNull(message = "ID cannot be null") @Min(value = 1, message = "ID must be positive") long id,
            @RequestBody @Valid UpdateEventDTO updateEventDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, updateEventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    // Only The Organizer Who Have The Event Can Delete it
    @PreAuthorize("hasRole('ADMIN') or @eventSecurity.isOrganizer(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable("id") @NotNull(message = "ID cannot be null") @Min(value = 1, message = "ID must be positive") long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
