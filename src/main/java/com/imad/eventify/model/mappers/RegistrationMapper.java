package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.entities.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "invitation.id", target = "invitationId")
    RegistrationDTO toDTO(Registration registration);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "eventId", target = "event.id")
    @Mapping(source = "invitationId", target = "invitation.id")
    Registration toEntity(RegistrationDTO dto);
}


