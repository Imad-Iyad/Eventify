package com.imad.eventify.model.entities;

import com.imad.eventify.model.entities.enums.EmailStatus;
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
@Table(name = "emails_logs")
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