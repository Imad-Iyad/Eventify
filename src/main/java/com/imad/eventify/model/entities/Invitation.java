package com.imad.eventify.model.entities;

import com.imad.eventify.model.entities.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    @OneToOne(mappedBy = "invitation")
    private Registration registration;

    private String inviteeEmail;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private LocalDateTime sentAt;
    private LocalDateTime respondedAt;

}

