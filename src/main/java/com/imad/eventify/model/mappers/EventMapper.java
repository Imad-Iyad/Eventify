package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventResponseDTO;
import com.imad.eventify.model.DTOs.UpdateEventDTO;
import com.imad.eventify.model.entities.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponseDTO toDTO(Event event);
    Event updateDTOtoEntity(UpdateEventDTO dto);
    Event creationDTOtoEntity(EventCreationRequest request);
}
