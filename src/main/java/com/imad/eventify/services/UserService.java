package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.RegisterRequest;
import com.imad.eventify.model.DTOs.UserDTO;
import com.imad.eventify.model.DTOs.UserResponseDTO;
import com.imad.eventify.model.entities.User;

import java.util.List;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
    UserResponseDTO getCurrentUserDTO();
    User getCurrentUserEntity();
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(UserDTO userDTO);
    String deactivateCurrentUser();
}

