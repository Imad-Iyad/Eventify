package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.entities.Registration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    RegistrationDTO toDTO(Registration registration);
    Registration toEntity(RegistrationDTO dto);
}

