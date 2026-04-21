package com.eschool.controller;

import com.eschool.entity.Student;
import com.eschool.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/enroll/{userId}")
    public Student enroll(@PathVariable Long userId,
                          @RequestParam String className,
                          @RequestParam String section) {
        return studentService.enrollStudent(userId, className, section);
    }
}