package com.imad.eventify.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Event event;


    private String registrationToken;

    @Lob
    private byte[] qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invitation_id", unique = true) // unique تمنع التكرار
    private Invitation invitation;

    private LocalDateTime registeredAt;
    private Boolean attendanceConfirmed;
}
