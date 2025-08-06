package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.InvitationDTO;
import com.imad.eventify.model.entities.Invitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvitationMapper {
    InvitationDTO toDTO(Invitation invitation);
    Invitation toEntity(InvitationDTO dto);
}

