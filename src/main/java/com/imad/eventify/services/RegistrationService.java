package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.RegistrationDTO;

public interface RegistrationService {
    RegistrationDTO registerToEvent(RegistrationDTO registrationDTO);
    RegistrationDTO getRegistrationByToken(String token);
}

