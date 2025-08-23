package com.imad.eventify.services;

import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.DTOs.RegistrationReqDTO;

public interface RegistrationService {
    RegistrationDTO registerToEvent(RegistrationReqDTO registrationReqDTO);
    RegistrationDTO getRegistrationByToken(String token);
}

