package com.eschool.controller;

import com.eschool.entity.SubjectMapping;
import com.eschool.entity.Teacher;
import com.eschool.repository.SubjectMappingRepository;
import com.eschool.repository.TeacherRepository; // Fixed: Missing import
import com.eschool.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/teachers")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminTeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectMappingRepository mappingRepository;

    @Autowired
    private TeacherRepository teacherRepository; // Fixed: Added this to avoid red lines

    // ================= TEACHER CRUD =================

    @PostMapping("/add")
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity.ok(teacherService.addTeacher(teacher));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacherDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("Teacher removed successfully!");
    }

    // ================= SUBJECT MAPPING =================

    @PostMapping("/assign-class")
    public ResponseEntity<SubjectMapping> assignClassToTeacher(@RequestBody SubjectMapping mapping) {
        // Fetch full teacher details to avoid nulls in response
        Teacher teacher = teacherRepository.findById(mapping.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        mapping.setTeacher(teacher);
        return ResponseEntity.ok(mappingRepository.save(mapping));
    }

    @GetMapping("/assignments/{teacherId}")
    public ResponseEntity<List<SubjectMapping>> getTeacherAssignments(@PathVariable Long teacherId) {
        return ResponseEntity.ok(mappingRepository.findByTeacherId(teacherId));
    }
}