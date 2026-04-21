package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FeeStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;   // e.g., "10th", "5th"
    private String feeType;     // e.g., "ADMISSION", "MONTHLY", "EXAM"
    private Double amount;      // e.g., 2000.0
}