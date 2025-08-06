package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.EmailRequestDTO;
import com.imad.eventify.model.entities.EmailLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailLogMapper {
    EmailLog toEntity(EmailRequestDTO dto);
}

