package com.imad.eventify.model.DTOs;

import lombok.Data;

@Data
public class RegistrationDTO {
    private Long id;
    private Long eventId;
    private Long userId;
    private Long invitationId;
    private String registrationToken;
}
