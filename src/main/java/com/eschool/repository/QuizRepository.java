package com.eschool.repository;

import com.eschool.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Class ke basis par saari quizzes dhoondne ke liye
    List<Quiz> findByClassName(String className);

    // Subject ke basis par filter karne ke liye
    List<Quiz> findBySubjectName(String subjectName);
}