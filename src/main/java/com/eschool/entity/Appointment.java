package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parentName;
    private String studentName;
    private String mobile;
    private LocalDateTime preferredDate;
    private String status; // PENDING, APPROVED, COMPLETED, CANCELLED

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User appliedBy; // Links back to the User who booked it
}