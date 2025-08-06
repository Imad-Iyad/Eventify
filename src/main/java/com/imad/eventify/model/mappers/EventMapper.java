package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.EventCreationRequest;
import com.imad.eventify.model.DTOs.EventDTO;
import com.imad.eventify.model.DTOs.EventSummaryDTO;
import com.imad.eventify.model.entities.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDTO toDTO(Event event);
    Event toEntity(EventDTO dto);

    EventSummaryDTO toSummaryDTO(Event event);
    Event toEntity(EventCreationRequest request);
}
