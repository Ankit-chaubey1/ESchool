package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SubjectMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // Kaunsa teacher?

    private String className; // e.g., 9th, 10th
    private String section;   // e.g., A, B
    private String subjectName; // e.g., Mathematics, Physics
}