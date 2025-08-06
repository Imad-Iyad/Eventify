package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.AttendanceConfirmationDTO;
import com.imad.eventify.model.entities.AttendanceConfirmation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceConfirmationMapper {
    AttendanceConfirmationDTO toDTO(AttendanceConfirmation entity);
    AttendanceConfirmation toEntity(AttendanceConfirmationDTO dto);
}

