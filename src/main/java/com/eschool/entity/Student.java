package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rollNumber; // Generated (e.g., SCH-2026-001)
    private String studentName;
    private String className;   // e.g., "Class 10"
    private String section;     // e.g., "A"

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Links the student record to their login account
}