package com.eschool.service;

import com.eschool.entity.Teacher;
import com.eschool.entity.User;
import com.eschool.enums.Role;
import com.eschool.repository.TeacherRepository;
import com.eschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Teacher addTeacher(Teacher teacher) {
        if (teacher.getUser() == null || teacher.getUser().getPassword() == null) {
            throw new RuntimeException("User account or Password details are missing!");
        }

        User user = teacher.getUser();

        // Yahan password encode ho raha hai
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_TEACHER);
user.setFullName(teacher.getName());
        teacher.setUser(user);

        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }

    public Teacher updateTeacher(Long id, Teacher details) {
        Teacher existingTeacher = getTeacherById(id);

        existingTeacher.setName(details.getName());
        existingTeacher.setSubjectSpecialization(details.getSubjectSpecialization());
        existingTeacher.setQualification(details.getQualification());
        existingTeacher.setPhoneNumber(details.getPhoneNumber());
        existingTeacher.setActive(details.isActive());

        return teacherRepository.save(existingTeacher);
    }
}