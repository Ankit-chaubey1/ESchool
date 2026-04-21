package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class StudentFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String description; // e.g., "Admission Fee" or "May Month Tuition Fee"
    private Double amount;
    private String status;      // "PENDING" or "PAID"

    private LocalDate dueDate;
    private LocalDate paidDate;
    private String transactionId; // UPI Ref Number (Optional)
    private String markedBy;      // Kaunse admin ne payment verify ki
}