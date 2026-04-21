package com.eschool.repository;

import com.eschool.entity.Student;
import com.eschool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByUserAndStudentName(User user, String studentName);

    // Class aur Section ke hisaab se bache nikalne ke liye
    List<Student> findByClassNameAndSection(String className, String section);

    // Poori class ke bache (saare sections)
    List<Student> findByClassName(String className);

}