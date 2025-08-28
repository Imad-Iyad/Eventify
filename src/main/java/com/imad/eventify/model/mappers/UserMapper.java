package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.UserDTO;
import com.imad.eventify.model.DTOs.UserResponseDTO;
import com.imad.eventify.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    UserResponseDTO toResponseDTO(User user);
    // تحديث جزئي: بيعمل set بس للحقول اللي مش null
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);
}

