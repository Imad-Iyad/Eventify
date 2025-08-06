package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}