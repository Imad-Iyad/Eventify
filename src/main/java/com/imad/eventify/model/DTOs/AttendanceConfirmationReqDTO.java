package com.imad.eventify.model.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceConfirmationReqDTO {
    @NotBlank(message = "Not Valid Token")
    private String token;
}