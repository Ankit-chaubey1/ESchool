package com.eschool.controller;

import com.eschool.entity.Attendance;
import com.eschool.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Sirf Teacher ya Admin hi attendance laga sakte hain
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/bulk-mark")
    public List<Attendance> markBulk(@RequestBody Map<Long, Boolean> attendanceData) {
        return attendanceService.markBulkAttendance(attendanceData);
    }

    // Parent aur Student sirf apni history DEKH sakte hain (Read-only)
    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN', 'ROLE_APPLICANT', 'ROLE_STUDENT')")
    @GetMapping("/student/{studentId}")
    public List<Attendance> getHistory(@PathVariable Long studentId) {
        return attendanceService.getStudentHistory(studentId);
    }
}