package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role; // ADMIN, ORGANIZER, ATTENDEE
}