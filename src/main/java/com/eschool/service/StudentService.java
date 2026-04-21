////package com.eschool.service;
////
////import com.eschool.entity.Student;
////import com.eschool.entity.User;
////import com.eschool.entity.Appointment;
////import com.eschool.repository.StudentRepository;
////import com.eschool.repository.UserRepository;
////import com.eschool.repository.AppointmentRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////@Service
////public class StudentService {
////
////    @Autowired
////    private StudentRepository studentRepository;
////
////    @Autowired
////    private AppointmentRepository appointmentRepository; // Injecting Appointment repo
////
////    public Student enrollStudent(Long appointmentId, String className, String section) {
////        // 1. Appointment dhundo
////        Appointment appointment = appointmentRepository.findById(appointmentId)
////                .orElseThrow(() -> new RuntimeException("Appointment not found"));
////
////        // 2. Duplicate Check (Jo humne pehle kiya tha)
////        if (studentRepository.existsByUserAndStudentName(appointment.getAppliedBy(), appointment.getStudentName())) {
////            throw new RuntimeException("Student already enrolled!");
////        }
////
////        // 3. Naya Student banao
////        Student student = new Student();
////        student.setUser(appointment.getAppliedBy());
////        student.setStudentName(appointment.getStudentName());
////        student.setClassName(className);
////        student.setSection(section);
////        student.setRollNumber("SCH-" + System.currentTimeMillis() % 10000);
////
////        Student savedStudent = studentRepository.save(student);
////
////        // 4. 🔥 IMPORTANT: Appointment ka status "APPROVED" se "ADMITTED" kar do
////        appointment.setStatus("ADMITTED");
////        appointmentRepository.save(appointment); // Update the status in DB
////
////        return savedStudent;
////    }
////}
//package com.eschool.service;
//
//import com.eschool.entity.Student;
//import com.eschool.entity.Appointment;
//import com.eschool.repository.StudentRepository;
//import com.eschool.repository.AppointmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class StudentService {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private FeeService feeService; // 🔥 FeeService ko inject kiya
//
//    @Transactional // Transactional zaroori hai taaki agar fee fail ho toh admission bhi na ho
//    public Student enrollStudent(Long appointmentId, String className, String section) {
//        // 1. Appointment dhundo
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new RuntimeException("Appointment not found"));
//
//        // 2. Duplicate Check
//        if (studentRepository.existsByUserAndStudentName(appointment.getAppliedBy(), appointment.getStudentName())) {
//            throw new RuntimeException("Student already enrolled!");
//        }
//
//        // 3. Naya Student banao
//        Student student = new Student();
//        student.setUser(appointment.getAppliedBy());
//        student.setStudentName(appointment.getStudentName());
//        student.setClassName(className); // Make sure your Student entity has setClassName or setStudentClass
//        student.setSection(section);
//        student.setRollNumber("SCH-" + System.currentTimeMillis() % 10000);
//
//        Student savedStudent = studentRepository.save(student);
//
//        // 4. 🔥 Automatic Fee Generation
//        // Jaise hi student save hua, uska admission bill phat jayega
//        feeService.generateAdmissionFee(savedStudent);
//
//        // 5. Appointment ka status update
//        appointment.setStatus("ADMITTED");
//        appointmentRepository.save(appointment);
//
//        return savedStudent;
//    }
//}

package com.eschool.service;

import com.eschool.entity.Student;
import com.eschool.entity.Appointment;
import com.eschool.repository.StudentRepository;
import com.eschool.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private FeeService feeService;

    @Transactional
    public Student enrollStudent(Long appointmentId, String className, String section) {
        logger.info("Starting enrollment process for Appointment ID: {}. Class: {}, Section: {}", appointmentId, className, section);

        // 1. Find Appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    logger.error("Enrollment failed: Appointment record {} not found.", appointmentId);
                    return new RuntimeException("Resource not found: Appointment record does not exist for the provided ID.");
                });

        // 2. Duplicate Check
        if (studentRepository.existsByUserAndStudentName(appointment.getAppliedBy(), appointment.getStudentName())) {
            logger.warn("Enrollment blocked: Student '{}' is already registered under this user account.", appointment.getStudentName());
            throw new RuntimeException("Data integrity error: A student with this name is already enrolled under this account.");
        }

        // 3. Create Student Entity
        Student student = new Student();
        student.setUser(appointment.getAppliedBy());
        student.setStudentName(appointment.getStudentName());
        student.setClassName(className);
        student.setSection(section);
        student.setRollNumber("SCH-" + (System.currentTimeMillis() % 10000));

        Student savedStudent = studentRepository.save(student);
        logger.info("Student record created successfully. Assigned Roll Number: {}", savedStudent.getRollNumber());

        // 4. Automatic Fee Generation
        try {
            feeService.generateAdmissionFee(savedStudent);
            logger.info("Initial admission fee generated for Student ID: {}", savedStudent.getId());
        } catch (Exception e) {
            logger.error("Fee generation failed during enrollment for Student ID: {}. Error: {}", savedStudent.getId(), e.getMessage());
            throw new RuntimeException("System error: Enrollment failed due to fee calculation error. Please verify fee structure.");
        }

        // 5. Update Appointment Status
        appointment.setStatus("ADMITTED");
        appointmentRepository.save(appointment);
        logger.info("Appointment ID: {} status updated to ADMITTED.", appointmentId);

        return savedStudent;
    }
}