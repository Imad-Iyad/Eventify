package com.imad.eventify.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeDTO {
    private Long id;
    private String name;
    private String email;
    private Boolean confirmed;
    private Long eventId;
}
//يمثل الحضور، سواء في فعاليات عامة أو خاصة.