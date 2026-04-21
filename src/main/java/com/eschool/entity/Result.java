package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String subjectName;
    private String examType;  // e.g., CLASS_TEST, MID_TERM, FINAL_EXAM
    private String examMode;  // e.g., ONLINE, OFFLINE

    private Double marksObtained;
    private Double totalMarks;
    private Double percentage;
    private String grade;
    private String remarks;
}