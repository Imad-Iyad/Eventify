package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String name;
    private Role role;
    private String message;
}
