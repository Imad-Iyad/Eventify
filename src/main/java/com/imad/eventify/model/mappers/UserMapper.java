package com.imad.eventify.model.mappers;

import com.imad.eventify.model.DTOs.UserDTO;
import com.imad.eventify.model.DTOs.UserResponseDTO;
import com.imad.eventify.model.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    UserResponseDTO toResponseDTO(User user);

    /*@Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDTO dto, @MappingTarget User user);*/
}

