package com.imad.eventify.model.entities;

import com.imad.eventify.model.entities.enums.EmailStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class EmailLog {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recipient_to")
    private String to;
    private String subject;
    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @ManyToOne(optional = true)
    private Event event;

    private LocalDateTime sentAt;
}