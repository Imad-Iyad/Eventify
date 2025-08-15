package com.imad.eventify.model.DTOs;

import com.imad.eventify.model.entities.enums.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {
    private Long id;
    private Long eventId;
    private String email; //inviteeEmail
    private InvitationStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime usedAt;
}

//للدعوات المرسلة في الفعاليات الخاصة فقط.