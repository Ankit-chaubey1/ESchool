package com.eschool.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user; // Isse email/password/role milega

    private String name;
    private String subjectSpecialization; // e.g., Math, Science
    private String qualification;         // e.g., M.Sc, B.Ed
    private String phoneNumber;
    private String joiningDate;
    private boolean isActive = true;      // Teacher school mein hai ya chhod gaya
}