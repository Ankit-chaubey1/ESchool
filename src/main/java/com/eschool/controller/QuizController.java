package com.eschool.controller;

import com.eschool.entity.*;
import com.eschool.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Sirf Admin aur Teacher create kar sakein
    @PostMapping("/create")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    // Student submit kar sake (SecurityConfig mein role define hai)
    @PostMapping("/{quizId}/submit/{studentId}")
    public ResponseEntity<Result> submitQuiz(
            @PathVariable Long quizId,
            @PathVariable Long studentId,
            @RequestBody Map<Long, String> answers) {
        return ResponseEntity.ok(quizService.submitQuiz(quizId, studentId, answers));
    }
}