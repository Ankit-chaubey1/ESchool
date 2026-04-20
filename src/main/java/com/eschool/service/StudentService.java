package com.eschool.service;

import com.eschool.entity.Student;
import com.eschool.entity.User;
import com.eschool.repository.StudentRepository;
import com.eschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    public Student enrollStudent(Long userId, String className, String section) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = new Student();
        student.setUser(user);
        student.setStudentName(user.getFullName());
        student.setClassName(className);
        student.setSection(section);

        // Generate a simple roll number
        student.setRollNumber("SCH-" + System.currentTimeMillis() % 10000);

        return studentRepository.save(student);
    }
}