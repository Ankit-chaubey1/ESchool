package com.eschool.controller;

import com.eschool.entity.Student;
import com.eschool.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
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