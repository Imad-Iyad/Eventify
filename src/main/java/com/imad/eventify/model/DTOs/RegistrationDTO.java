package com.imad.eventify.model.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    @NotNull(message = "eventId cannot be null")
    @Min(value = 1, message = "eventId must be positive")
    private Long eventId;

    @NotNull(message = "invitationId cannot be null")
    @Min(value = 1, message = "invitationId must be positive")
    private Long invitationId;

 // private String inviteeEmail;
}
