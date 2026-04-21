package com.eschool.repository;

import com.eschool.entity.StudentFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentFeeRepository extends JpaRepository<StudentFee, Long> {
    List<StudentFee> findByStudentId(Long studentId);
}
