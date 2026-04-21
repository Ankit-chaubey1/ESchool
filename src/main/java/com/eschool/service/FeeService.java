//package com.eschool.service;
//
//import com.eschool.entity.*;
//import com.eschool.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class FeeService {
//
//    @Autowired
//    private StudentFeeRepository studentFeeRepository;
//
//    @Autowired
//    private FeeStructureRepository feeStructureRepository;
//
//    // 1. Bill Generate karne ka logic
//    public void generateAdmissionFee(Student student) {
//        // Fee Structure se rate uthao (e.g., 10th class ki ADMISSION fee kitni hai)
//        FeeStructure structure = feeStructureRepository
//                .findByClassNameAndFeeType(student.getClassName(), "ADMISSION")
//                .orElseThrow(() -> new RuntimeException("Fee structure not set for this class"));
//
//        StudentFee fee = new StudentFee();
//        fee.setStudent(student);
//        fee.setDescription("New Admission Fee - " + student.getClassName());
//        fee.setAmount(structure.getAmount());
//        fee.setStatus("PENDING");
//        fee.setDueDate(LocalDate.now().plusDays(7)); // 7 din ka time payment ke liye
//
//        studentFeeRepository.save(fee);
//    }
//
//    // 2. Payment Verify karke PAID mark karne ka logic
//    public StudentFee markAsPaid(Long feeId, String transactionId) {
//        StudentFee fee = studentFeeRepository.findById(feeId)
//                .orElseThrow(() -> new RuntimeException("Fee record not found"));
//
//        // Kaunsa admin verify kar raha hai?
//        String adminName = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        fee.setStatus("PAID");
//        fee.setPaidDate(LocalDate.now());
//        fee.setTransactionId(transactionId); // UPI Ref no. ya "CASH"
//        fee.setMarkedBy(adminName);
//
//        return studentFeeRepository.save(fee);
//    }
//
//    public List<StudentFee> getFeesByStudent(Long studentId) {
//        return studentFeeRepository.findByStudentId(studentId);
//    }
//    @Autowired
//    private StudentRepository studentRepository; // Saare bacho ko nikalne ke liye
//
//    // Har mahine ki 1st date ko raat 12 baje ye apne aap chalega
//    @Scheduled(cron = "0 0 0 1 * ?")   //@Scheduled(fixedRate = 60000) (Ye har 1 minute mein chalega).
//    public void generateMonthlyFees() {
//        System.out.println("Cron Job Started: Generating Monthly Fees...");
//
//        // 1. Saare students ki list nikalo
//        List<Student> allStudents = studentRepository.findAll();
//        LocalDate today = LocalDate.now();
//        String monthName = today.getMonth().toString();
//        int year = today.getYear();
//
//        for (Student student : allStudents) {
//            try {
//                // 2. FeeStructure se "MONTHLY" rate uthao us student ki class ke liye
//                FeeStructure structure = (FeeStructure) feeStructureRepository
//                        .findByClassNameIgnoreCaseAndFeeTypeIgnoreCase(student.getClassName(), "MONTHLY")
//                        .orElseThrow(() -> new RuntimeException("Monthly fee not set for class: " + student.getClassName()));
//
//                // 3. Naya bill (StudentFee) banao
//                StudentFee fee = new StudentFee();
//                fee.setStudent(student);
//                fee.setDescription("Monthly Tuition Fee - " + monthName + " " + year);
//                fee.setAmount(structure.getAmount());
//                fee.setStatus("PENDING");
//                fee.setDueDate(today.withDayOfMonth(15)); // 15 tarikh tak last date set kar di
//
//                studentFeeRepository.save(fee);
//
//            } catch (Exception e) {
//                // Agar kisi bache ki class ka rate nahi mila toh error log kar do, skip mat karo
//                System.err.println("Error for student " + student.getRollNumber() + ": " + e.getMessage());
//            }
//        }
//        System.out.println("Cron Job Finished!");
//    }
//}

package com.eschool.service;

import com.eschool.entity.*;
import com.eschool.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeeService {

    private static final Logger logger = LoggerFactory.getLogger(FeeService.class);

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private StudentRepository studentRepository;

    public void generateAdmissionFee(Student student) {
        logger.info("Generating admission fee for student: {} (ID: {})", student.getStudentName(), student.getId());

        FeeStructure structure = feeStructureRepository
                .findByClassNameAndFeeType(student.getClassName(), "ADMISSION")
                .orElseThrow(() -> {
                    logger.error("Admission fee generation failed: No fee structure found for class {}", student.getClassName());
                    return new RuntimeException("Fee structure configuration missing for class: " + student.getClassName());
                });

        StudentFee fee = new StudentFee();
        fee.setStudent(student);
        fee.setDescription("New Admission Fee - " + student.getClassName());
        fee.setAmount(structure.getAmount());
        fee.setStatus("PENDING");
        fee.setDueDate(LocalDate.now().plusDays(7));

        studentFeeRepository.save(fee);
        logger.info("Admission fee record created successfully for Student ID: {}", student.getId());
    }

    public StudentFee markAsPaid(Long feeId, String transactionId) {
        logger.info("Processing fee payment verification for Fee ID: {}", feeId);

        StudentFee fee = studentFeeRepository.findById(feeId)
                .orElseThrow(() -> {
                    logger.error("Payment verification failed: Fee record ID {} not found", feeId);
                    return new RuntimeException("Resource not found: Fee record with provided ID does not exist.");
                });

        String adminName = SecurityContextHolder.getContext().getAuthentication().getName();

        fee.setStatus("PAID");
        fee.setPaidDate(LocalDate.now());
        fee.setTransactionId(transactionId);
        fee.setMarkedBy(adminName);

        StudentFee updatedFee = studentFeeRepository.save(fee);
        logger.info("Fee ID {} marked as PAID by Admin: {}. Transaction ID: {}", feeId, adminName, transactionId);

        return updatedFee;
    }

    public List<StudentFee> getFeesByStudent(Long studentId) {
        logger.info("Fetching fee history for Student ID: {}", studentId);
        return studentFeeRepository.findByStudentId(studentId);
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyFees() {
        logger.info("Automated Task Started: Monthly fee generation process initiated.");

        List<Student> allStudents = studentRepository.findAll();
        LocalDate today = LocalDate.now();
        String monthName = today.getMonth().toString();
        int year = today.getYear();

        for (Student student : allStudents) {
            try {
                FeeStructure structure = (FeeStructure) feeStructureRepository
                        .findByClassNameIgnoreCaseAndFeeTypeIgnoreCase(student.getClassName(), "MONTHLY")
                        .orElseThrow(() -> new RuntimeException("Missing monthly fee structure for class: " + student.getClassName()));

                StudentFee fee = new StudentFee();
                fee.setStudent(student);
                fee.setDescription("Monthly Tuition Fee - " + monthName + " " + year);
                fee.setAmount(structure.getAmount());
                fee.setStatus("PENDING");
                fee.setDueDate(today.withDayOfMonth(15));

                studentFeeRepository.save(fee);
                logger.debug("Monthly fee generated for Roll No: {}", student.getRollNumber());

            } catch (Exception e) {
                logger.error("Skipping fee generation for Student {}: {}", student.getRollNumber(), e.getMessage());
            }
        }
        logger.info("Automated Task Finished: Monthly fee generation completed.");
    }
}