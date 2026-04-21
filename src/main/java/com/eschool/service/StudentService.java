//package com.eschool.service;
//
//import com.eschool.entity.Student;
//import com.eschool.entity.User;
//import com.eschool.entity.Appointment;
//import com.eschool.repository.StudentRepository;
//import com.eschool.repository.UserRepository;
//import com.eschool.repository.AppointmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StudentService {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository; // Injecting Appointment repo
//
//    public Student enrollStudent(Long appointmentId, String className, String section) {
//        // 1. Appointment dhundo
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new RuntimeException("Appointment not found"));
//
//        // 2. Duplicate Check (Jo humne pehle kiya tha)
//        if (studentRepository.existsByUserAndStudentName(appointment.getAppliedBy(), appointment.getStudentName())) {
//            throw new RuntimeException("Student already enrolled!");
//        }
//
//        // 3. Naya Student banao
//        Student student = new Student();
//        student.setUser(appointment.getAppliedBy());
//        student.setStudentName(appointment.getStudentName());
//        student.setClassName(className);
//        student.setSection(section);
//        student.setRollNumber("SCH-" + System.currentTimeMillis() % 10000);
//
//        Student savedStudent = studentRepository.save(student);
//
//        // 4. 🔥 IMPORTANT: Appointment ka status "APPROVED" se "ADMITTED" kar do
//        appointment.setStatus("ADMITTED");
//        appointmentRepository.save(appointment); // Update the status in DB
//
//        return savedStudent;
//    }
//}
package com.eschool.service;

import com.eschool.entity.Student;
import com.eschool.entity.Appointment;
import com.eschool.repository.StudentRepository;
import com.eschool.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private FeeService feeService; // 🔥 FeeService ko inject kiya

    @Transactional // Transactional zaroori hai taaki agar fee fail ho toh admission bhi na ho
    public Student enrollStudent(Long appointmentId, String className, String section) {
        // 1. Appointment dhundo
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 2. Duplicate Check
        if (studentRepository.existsByUserAndStudentName(appointment.getAppliedBy(), appointment.getStudentName())) {
            throw new RuntimeException("Student already enrolled!");
        }

        // 3. Naya Student banao
        Student student = new Student();
        student.setUser(appointment.getAppliedBy());
        student.setStudentName(appointment.getStudentName());
        student.setClassName(className); // Make sure your Student entity has setClassName or setStudentClass
        student.setSection(section);
        student.setRollNumber("SCH-" + System.currentTimeMillis() % 10000);

        Student savedStudent = studentRepository.save(student);

        // 4. 🔥 Automatic Fee Generation
        // Jaise hi student save hua, uska admission bill phat jayega
        feeService.generateAdmissionFee(savedStudent);

        // 5. Appointment ka status update
        appointment.setStatus("ADMITTED");
        appointmentRepository.save(appointment);

        return savedStudent;
    }
}