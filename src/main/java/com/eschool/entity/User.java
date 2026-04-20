package com.eschool.entity;

import com.eschool.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Email or Phone

    @Column(nullable = false)
    @JsonIgnore
    private String password; // Will be stored as HASH

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, TEACHER, STUDENT, APPLICANT

    private boolean enabled = true;
}
