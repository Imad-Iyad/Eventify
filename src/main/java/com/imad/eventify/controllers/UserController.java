package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.UserDTO;
import com.imad.eventify.model.DTOs.UserResponseDTO;
import com.imad.eventify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Only ADMIN can see all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Any User
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUserDTO();
        return ResponseEntity.ok(user);
    }

    // Any User
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Any User
    @DeleteMapping
    public ResponseEntity<String> deactivateCurrentUser() {
        String message = userService.deactivateCurrentUser();
        return ResponseEntity.ok(message);
    }
}
