package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.InvitationResponseDTO;
import com.imad.eventify.model.entities.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface InvitationMapper {

    @Mapping(source = "event.id", target = "eventId")
    InvitationResponseDTO toResponseDTO(Invitation invitation);
}

