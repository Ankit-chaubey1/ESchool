//package com.eschool.service;
//
//import com.eschool.entity.Teacher;
//import com.eschool.entity.User;
//import com.eschool.enums.Role;
//import com.eschool.repository.TeacherRepository;
//import com.eschool.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TeacherService {
//
//    @Autowired
//    private TeacherRepository teacherRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public Teacher addTeacher(Teacher teacher) {
//        if (teacher.getUser() == null || teacher.getUser().getPassword() == null) {
//            throw new RuntimeException("User account or Password details are missing!");
//        }
//
//        User user = teacher.getUser();
//
//        // Yahan password encode ho raha hai
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(Role.ROLE_TEACHER);
//user.setFullName(teacher.getName());
//        teacher.setUser(user);
//
//        return teacherRepository.save(teacher);
//    }
//
//    public List<Teacher> getAllTeachers() {
//        return teacherRepository.findAll();
//    }
//
//    public void deleteTeacher(Long id) {
//        teacherRepository.deleteById(id);
//    }
//    public Teacher getTeacherById(Long id) {
//        return teacherRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
//    }
//
//    public Teacher updateTeacher(Long id, Teacher details) {
//        Teacher existingTeacher = getTeacherById(id);
//
//        existingTeacher.setName(details.getName());
//        existingTeacher.setSubjectSpecialization(details.getSubjectSpecialization());
//        existingTeacher.setQualification(details.getQualification());
//        existingTeacher.setPhoneNumber(details.getPhoneNumber());
//        existingTeacher.setActive(details.isActive());
//
//        return teacherRepository.save(existingTeacher);
//    }
//}

package com.eschool.service;

import com.eschool.entity.Teacher;
import com.eschool.entity.User;
import com.eschool.enums.Role;
import com.eschool.repository.TeacherRepository;
import com.eschool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Teacher addTeacher(Teacher teacher) {
        logger.info("Attempting to register a new teacher: {}", teacher.getName());

        if (teacher.getUser() == null || teacher.getUser().getPassword() == null) {
            logger.error("Teacher registration failed: Missing user credentials or password.");
            throw new RuntimeException("Invalid request: User account details and password are required for teacher registration.");
        }

        User user = teacher.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_TEACHER);
        user.setFullName(teacher.getName());

        teacher.setUser(user);

        Teacher savedTeacher = teacherRepository.save(teacher);
        logger.info("Teacher successfully registered with ID: {} and linked to User: {}", savedTeacher.getId(), user.getUsername());

        return savedTeacher;
    }

    public List<Teacher> getAllTeachers() {
        logger.info("Fetching all teacher records.");
        return teacherRepository.findAll();
    }

    public void deleteTeacher(Long id) {
        logger.warn("Deleting teacher record with ID: {}", id);
        if (!teacherRepository.existsById(id)) {
            logger.error("Delete failed: Teacher ID {} does not exist.", id);
            throw new RuntimeException("Resource not found: Cannot delete teacher. ID " + id + " not found.");
        }
        teacherRepository.deleteById(id);
        logger.info("Teacher record ID: {} deleted successfully.", id);
    }

    public Teacher getTeacherById(Long id) {
        logger.info("Retrieving details for Teacher ID: {}", id);
        return teacherRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Teacher ID {} not found.", id);
                    return new RuntimeException("Resource not found: Teacher record not found for the provided ID.");
                });
    }

    public Teacher updateTeacher(Long id, Teacher details) {
        logger.info("Updating profile for Teacher ID: {}", id);
        Teacher existingTeacher = getTeacherById(id);

        existingTeacher.setName(details.getName());
        existingTeacher.setSubjectSpecialization(details.getSubjectSpecialization());
        existingTeacher.setQualification(details.getQualification());
        existingTeacher.setPhoneNumber(details.getPhoneNumber());
        existingTeacher.setActive(details.isActive());

        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        logger.info("Teacher profile ID: {} updated successfully.", id);

        return updatedTeacher;
    }
}