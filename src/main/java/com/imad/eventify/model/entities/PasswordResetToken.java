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
@Table
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    @ManyToOne
    private User user;

}