package com.eschool.controller;

import com.eschool.entity.Result;
import com.eschool.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    // 1. Manual Bulk Entry (Teacher paper check karke yahan se marks daalega)
    @PostMapping("/bulk-entry")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<Result>> addResults(@RequestBody List<Result> results) {
        return ResponseEntity.ok(resultService.saveAllResults(results));
    }

    // 2. Student apna result dekh sake
    @GetMapping("/student/{studentId}")

//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER','STUDENT')")
    public ResponseEntity<List<Result>> getStudentResults(@PathVariable Long studentId) {
        // Idhar aap ResultRepository se findByStudentId call kar lena
        return ResponseEntity.ok(resultService.getResultsByStudent(studentId));
    }
}