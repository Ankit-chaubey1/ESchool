package com.eschool.repository;

import com.eschool.entity.SubjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectMappingRepository extends JpaRepository<SubjectMapping, Long> {
    // Teacher ki ID se uski saari mapped classes nikalne ke liye
    List<SubjectMapping> findByTeacherId(Long teacherId);

    // Kisi particular class aur subject ka teacher dhoondne ke liye
    List<SubjectMapping> findByClassNameAndSection(String className, String section);
}