package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.RegistrationResDTO;
import com.imad.eventify.model.DTOs.RegistrationDTO;

public interface RegistrationService {
    RegistrationResDTO registerToEvent(RegistrationDTO registrationDTO);
    RegistrationResDTO getRegistrationByToken(String token);
}

