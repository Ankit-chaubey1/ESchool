package com.eschool.repository;

import com.eschool.entity.Student;
import com.eschool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByUserAndStudentName(User user, String studentName);
}