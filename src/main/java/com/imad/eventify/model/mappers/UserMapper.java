package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.UserDTO;
import com.imad.eventify.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
}

