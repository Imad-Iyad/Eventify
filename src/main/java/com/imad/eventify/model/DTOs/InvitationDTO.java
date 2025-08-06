package com.imad.eventify.model.DTOs;

import lombok.Data;

@Data
public class InvitationDTO {
    private Long id;
    private String inviteeEmail;
    private boolean accepted;
    private String token;
    private Long eventId;
}
//للدعوات المرسلة في الفعاليات الخاصة فقط.