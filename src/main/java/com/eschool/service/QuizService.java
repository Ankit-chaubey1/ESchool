//package com.eschool.service;
//
//import com.eschool.entity.*;
//import com.eschool.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class QuizService {
//
//    @Autowired
//    private QuizRepository quizRepository;
//
//    @Autowired
//    private ResultService resultService;
//
//    // 1. Quiz Create karna (Questions ke saath)
//    public Quiz createQuiz(Quiz quiz) {
//        return quizRepository.save(quiz);
//    }
//
//    // 2. Quiz Submit aur Auto-Correction Logic
//    public Result submitQuiz(Long quizId, Long studentId, Map<Long, String> answers) {
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        double marksObtained = 0;
//        int marksPerQuestion = quiz.getTotalMarks() / quiz.getQuestions().size();
//
//        // Har question ko check karo
//        for (Question q : quiz.getQuestions()) {
//            String submittedAnswer = answers.get(q.getId());
//            if (submittedAnswer != null && submittedAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
//                marksObtained += marksPerQuestion;
//            }
//        }
//
//        // Ab is score ko "Result" table mein save karwao (Using ResultService)
//        Result quizResult = new Result();
//        Student student = new Student(); student.setId(studentId);
//
//        quizResult.setStudent(student);
//        quizResult.setSubjectName(quiz.getSubjectName());
//        quizResult.setExamType("CLASS_TEST");
//        quizResult.setExamMode("ONLINE");
//        quizResult.setMarksObtained(marksObtained);
//        quizResult.setTotalMarks(Double.valueOf(quiz.getTotalMarks()));
//        quizResult.setRemarks("Auto-graded Online Quiz: " + quiz.getTitle());
//
//        return resultService.saveResult(quizResult); // Isme Percentage aur Grade khud calculate ho jayenge
//    }
//}

package com.eschool.service;

import com.eschool.entity.*;
import com.eschool.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ResultService resultService;

    public Quiz createQuiz(Quiz quiz) {
        logger.info("Creating new quiz: {} for subject: {}", quiz.getTitle(), quiz.getSubjectName());
        return quizRepository.save(quiz);
    }

    public Result submitQuiz(Long quizId, Long studentId, Map<Long, String> answers) {
        logger.info("Quiz submission received. Quiz ID: {}, Student ID: {}", quizId, studentId);

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> {
                    logger.error("Submission failed: Quiz ID {} not found", quizId);
                    return new RuntimeException("Resource not found: The specified quiz does not exist.");
                });

        double marksObtained = 0;
        int totalQuestions = quiz.getQuestions().size();

        if (totalQuestions == 0) {
            logger.warn("Quiz ID {} has no questions. Score will be 0.", quizId);
        } else {
            int marksPerQuestion = quiz.getTotalMarks() / totalQuestions;

            for (Question q : quiz.getQuestions()) {
                String submittedAnswer = answers.get(q.getId());
                if (submittedAnswer != null && submittedAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
                    marksObtained += marksPerQuestion;
                }
            }
        }

        Result quizResult = new Result();
        Student student = new Student();
        student.setId(studentId);

        quizResult.setStudent(student);
        quizResult.setSubjectName(quiz.getSubjectName());
        quizResult.setExamType("CLASS_TEST");
        quizResult.setExamMode("ONLINE");
        quizResult.setMarksObtained(marksObtained);
        quizResult.setTotalMarks(Double.valueOf(quiz.getTotalMarks()));
        quizResult.setRemarks("Auto-graded Online Quiz: " + quiz.getTitle());

        logger.info("Quiz evaluation complete. Student ID: {} scored {} marks.", studentId, marksObtained);
        return resultService.saveResult(quizResult);
    }
}