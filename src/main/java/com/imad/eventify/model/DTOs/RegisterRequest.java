package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must contain uppercase, lowercase, number, and special character"
    )
    private String password;

    @Schema(allowableValues = {"ADMIN", "ORGANIZER", "ATTENDEE"})
    @NotNull(message = "Role is required")
    private Role role; // ADMIN, ORGANIZER, ATTENDEE
}