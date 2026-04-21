package com.eschool.repository;

import com.eschool.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);

    // Check if attendance already exists for a student on a specific date
    Optional<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
}