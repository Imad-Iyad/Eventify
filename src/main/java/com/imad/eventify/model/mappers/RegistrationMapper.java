package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.RegistrationResDTO;
import com.imad.eventify.model.entities.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "invitation.id", target = "invitationId")
    RegistrationResDTO toDTO(Registration registration);
}
