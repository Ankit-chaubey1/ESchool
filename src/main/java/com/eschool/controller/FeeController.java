package com.eschool.controller;

import com.eschool.entity.StudentFee;
import com.eschool.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
public class FeeController {

    @Autowired
    private FeeService feeService;

    // Admin jab payment confirm karega
    @PostMapping("/pay/{feeId}")
    public StudentFee payFee(@PathVariable Long feeId, @RequestParam String transactionId) {
        return feeService.markAsPaid(feeId, transactionId);
    }

    // Parent/Student apna bill dekhne ke liye
    @GetMapping("/student/{studentId}")
    public List<StudentFee> getStudentFees(@PathVariable Long studentId) {
        return feeService.getFeesByStudent(studentId);
    }
}