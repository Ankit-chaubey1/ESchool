//package com.eschool.service;
//
//import com.eschool.entity.Attendance;
//import com.eschool.entity.Student;
//import com.eschool.repository.AttendanceRepository;
//import com.eschool.repository.StudentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
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
//    // Bulk Marking: map contains {studentId: isPresent}
//    public List<Attendance> markBulkAttendance(Map<Long, Boolean> attendanceData) {
//        List<Attendance> savedRecords = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//
//        for (Map.Entry<Long, Boolean> entry : attendanceData.entrySet()) {
//            Long studentId = entry.getKey();
//            Boolean present = entry.getValue();
//
//            Student student = studentRepository.findById(studentId)
//                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
//
//            // Check if record already exists for today
//            Attendance attendance = attendanceRepository.findByStudentIdAndDate(studentId, today)
//                    .orElse(new Attendance()); // If not exists, create new
//
//            attendance.setStudent(student);
//            attendance.setDate(today);
//            attendance.setPresent(present);
//            attendance.setMarkedBy(currentUsername);
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

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Attendance> markBulkAttendance(Map<Long, Boolean> attendanceData) {
        List<Attendance> savedRecords = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 1. Logged-in Teacher ka naam nikalne ka logic (Loop se pehle ek hi baar)
        String teacherName = "SYSTEM"; // Default agar security context na mile

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                teacherName = ((UserDetails) principal).getUsername();
            } else {
                teacherName = principal.toString();
            }
        }

        // 2. Loop chala kar har bache ki attendance process karo
        for (Map.Entry<Long, Boolean> entry : attendanceData.entrySet()) {
            Long studentId = entry.getKey();
            Boolean present = entry.getValue();

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

            // Check if record already exists for today to avoid duplicates
            Attendance attendance = attendanceRepository.findByStudentIdAndDate(studentId, today)
                    .orElse(new Attendance());

            attendance.setStudent(student);
            attendance.setDate(today);
            attendance.setPresent(present);
            attendance.setMarkedBy(teacherName); // Yahan teacher ka naam save ho raha hai

            savedRecords.add(attendanceRepository.save(attendance));
        }
        return savedRecords;
    }

    public List<Attendance> getStudentHistory(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}