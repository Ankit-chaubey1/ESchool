//
//package com.eschool.service;
//
//import com.eschool.entity.Attendance;
//import com.eschool.entity.Student;
//import com.eschool.repository.AttendanceRepository;
//import com.eschool.repository.StudentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class AttendanceService {
//
//    @Autowired
//    private AttendanceRepository attendanceRepository;
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    public List<Attendance> markBulkAttendance(Map<Long, Boolean> attendanceData) {
//        List<Attendance> savedRecords = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//
//        // 1. Logged-in Teacher ka naam nikalne ka logic (Loop se pehle ek hi baar)
//        String teacherName = "SYSTEM"; // Default agar security context na mile
//
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal instanceof UserDetails) {
//                teacherName = ((UserDetails) principal).getUsername();
//            } else {
//                teacherName = principal.toString();
//            }
//        }
//
//        // 2. Loop chala kar har bache ki attendance process karo
//        for (Map.Entry<Long, Boolean> entry : attendanceData.entrySet()) {
//            Long studentId = entry.getKey();
//            Boolean present = entry.getValue();
//
//            Student student = studentRepository.findById(studentId)
//                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
//
//            // Check if record already exists for today to avoid duplicates
//            Attendance attendance = attendanceRepository.findByStudentIdAndDate(studentId, today)
//                    .orElse(new Attendance());
//
//            attendance.setStudent(student);
//            attendance.setDate(today);
//            attendance.setPresent(present);
//            attendance.setMarkedBy(teacherName); // Yahan teacher ka naam save ho raha hai
//
//            savedRecords.add(attendanceRepository.save(attendance));
//        }
//        return savedRecords;
//    }
//
//    public List<Attendance> getStudentHistory(Long studentId) {
//        return attendanceRepository.findByStudentId(studentId);
//    }
//}

package com.eschool.service;

import com.eschool.entity.Attendance;
import com.eschool.entity.Student;
import com.eschool.repository.AttendanceRepository;
import com.eschool.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Attendance> markBulkAttendance(Map<Long, Boolean> attendanceData) {
        logger.info("Initiating bulk attendance marking process for {} students.", attendanceData.size());
        List<Attendance> savedRecords = new ArrayList<>();
        LocalDate today = LocalDate.now();

        String teacherName = "SYSTEM";

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                teacherName = ((UserDetails) principal).getUsername();
            } else {
                teacherName = principal.toString();
            }
        }
        logger.debug("Attendance is being marked by: {}", teacherName);

        for (Map.Entry<Long, Boolean> entry : attendanceData.entrySet()) {
            Long studentId = entry.getKey();
            Boolean present = entry.getValue();

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        logger.error("Attendance processing failed: Student with ID {} not found.", studentId);
                        return new RuntimeException("Data integrity error: Student record not found for ID " + studentId);
                    });

            Attendance attendance = attendanceRepository.findByStudentIdAndDate(studentId, today)
                    .orElse(new Attendance());

            attendance.setStudent(student);
            attendance.setDate(today);
            attendance.setPresent(present);
            attendance.setMarkedBy(teacherName);

            savedRecords.add(attendanceRepository.save(attendance));
            logger.debug("Attendance record saved for Student ID: {}, Status: {}", studentId, present ? "PRESENT" : "ABSENT");
        }

        logger.info("Bulk attendance processing completed successfully for date: {}", today);
        return savedRecords;
    }

    public List<Attendance> getStudentHistory(Long studentId) {
        logger.info("Retrieving attendance history for Student ID: {}", studentId);
        List<Attendance> history = attendanceRepository.findByStudentId(studentId);
        logger.info("Found {} attendance records for Student ID: {}", history.size(), studentId);
        return history;
    }
}