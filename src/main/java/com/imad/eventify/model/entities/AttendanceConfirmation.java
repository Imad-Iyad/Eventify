package com.imad.eventify.model.entities;

import com.imad.eventify.model.entities.enums.ConfirmationMethods;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_confirmations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // المستخدم الذي حضر
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // الفعالية التي حضرها
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    // وقت الحضور
    private LocalDateTime confirmedAt;

    // تم التأكيد عن طريق QR أو رابط
    private ConfirmationMethods confirmationMethod;
}

