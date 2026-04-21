package com.eschool.controller;

import com.eschool.entity.Student;
import com.eschool.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminStudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. Filter Students: /api/admin/students/filter?className=9th&section=A
    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudents(
            @RequestParam String className,
            @RequestParam(required = false) String section) {

        if (section != null && !section.isEmpty()) {
            return ResponseEntity.ok(studentRepository.findByClassNameAndSection(className, section));
        }
        return ResponseEntity.ok(studentRepository.findByClassName(className));
    }

    // 2. Get All Students (Pagination ke bina abhi ke liye)
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    // 3. Update Student: Roll No change karna ya Section promote karna
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedData) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

        // Jo fields update karni hain
        student.setClassName(updatedData.getClassName());
        student.setSection(updatedData.getSection());
        student.setRollNumber(updatedData.getRollNumber());


        return ResponseEntity.ok(studentRepository.save(student));
    }

    // 4. Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return ResponseEntity.ok("Student record deleted successfully!");
    }
}