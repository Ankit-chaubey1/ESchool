//package com.eschool.service;
//
//import com.eschool.entity.Result;
//import com.eschool.repository.ResultRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ResultService {
//
//    @Autowired
//    private ResultRepository resultRepository;
//
//    public Result saveResult(Result result) {
//        // 1. Automatic Percentage Calculation
//        if (result.getTotalMarks() != null && result.getTotalMarks() > 0) {
//            double percentage = (result.getMarksObtained() / result.getTotalMarks()) * 100;
//            result.setPercentage(percentage);
//
//            // 2. Automatic Grade Logic
//            result.setGrade(calculateGrade(percentage));
//        }
//
//        return resultRepository.save(result);
//    }
//
//    // Ek saath poori class ke marks save karne ke liye (Bulk Entry)
//    public List<Result> saveAllResults(List<Result> results) {
//        results.forEach(this::saveResult); // Har result par calculation chalegi
//        return resultRepository.saveAll(results);
//    }
//
//    // Helper method for grading
//    private String calculateGrade(double pct) {
//        if (pct >= 90) return "A+";
//        if (pct >= 80) return "A";
//        if (pct >= 70) return "B";
//        if (pct >= 60) return "C";
//        if (pct >= 50) return "D";
//        return "F";
//    }
//    public List<Result> getResultsByStudent(Long studentId) {
//        List<Result> results = resultRepository.findByStudentId(studentId);
//        if (results.isEmpty()) {
//            throw new RuntimeException("No results found for Student ID: " + studentId);
//        }
//        return results;
//    }
//}

package com.eschool.service;

import com.eschool.entity.Result;
import com.eschool.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private static final Logger logger = LoggerFactory.getLogger(ResultService.class);

    @Autowired
    private ResultRepository resultRepository;

    public Result saveResult(Result result) {
        logger.debug("Calculating results for Student ID: {}", result.getStudent().getId());

        if (result.getTotalMarks() != null && result.getTotalMarks() > 0) {
            double percentage = (result.getMarksObtained() / result.getTotalMarks()) * 100;
            result.setPercentage(percentage);
            result.setGrade(calculateGrade(percentage));
            logger.debug("Calculation complete: Percentage: {}%, Grade: {}", percentage, result.getGrade());
        }

        return resultRepository.save(result);
    }

    public List<Result> saveAllResults(List<Result> results) {
        logger.info("Processing bulk result entry for {} records.", results.size());
        results.forEach(this::saveResult);
        return resultRepository.saveAll(results);
    }

    private String calculateGrade(double pct) {
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        return "F";
    }

    public List<Result> getResultsByStudent(Long studentId) {
        logger.info("Fetching all academic results for Student ID: {}", studentId);
        List<Result> results = resultRepository.findByStudentId(studentId);

        if (results.isEmpty()) {
            logger.warn("No result records found for Student ID: {}", studentId);
            throw new RuntimeException("Data not found: No academic records exist for the provided Student ID.");
        }

        return results;
    }
}