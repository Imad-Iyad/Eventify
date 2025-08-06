package com.imad.eventify.model.DTOs;

import lombok.Data;

@Data
public class AttendeeDTO {
    private Long id;
    private String name;
    private String email;
    private Boolean confirmed;
    private Long eventId;
}
//يمثل الحضور، سواء في فعاليات عامة أو خاصة.