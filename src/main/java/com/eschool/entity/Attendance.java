package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date; // The date of the class
    private boolean isPresent; // true = Present, false = Absent

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student; // Which student does this record belong to?
    private String markedBy;
}