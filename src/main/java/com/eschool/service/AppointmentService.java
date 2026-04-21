//package com.eschool.service;
//
//import com.eschool.entity.Appointment;
//import com.eschool.entity.User;
//import com.eschool.repository.AppointmentRepository;
//import com.eschool.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class AppointmentService {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public Appointment bookAppointment(Appointment appointment, String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        appointment.setAppliedBy(user);
//        appointment.setStatus("PENDING");
//        return appointmentRepository.save(appointment);
//    }
//    public Appointment updateStatus(Long appointmentId, String newStatus) {
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new RuntimeException("Appointment not found"));
//
//        appointment.setStatus(newStatus);
//        return appointmentRepository.save(appointment);
//    }
//
//    public List<Appointment> getAllAppointments() {
//        return appointmentRepository.findAll();
//    }
//    public List<Appointment> getMyAppointments(String username) {
//        return appointmentRepository.findByAppliedByUsername(username);
//    }
//}

package com.eschool.service;

import com.eschool.entity.Appointment;
import com.eschool.entity.User;
import com.eschool.repository.AppointmentRepository;
import com.eschool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public Appointment bookAppointment(Appointment appointment, String username) {
        logger.info("Processing appointment booking request for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Appointment booking failed: User '{}' not found.", username);
                    return new RuntimeException("User authorization error: The requesting user does not exist.");
                });

        appointment.setAppliedBy(user);
        appointment.setStatus("PENDING");

        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Appointment successfully booked. Appointment ID: {}, Status: {}", savedAppointment.getId(), savedAppointment.getStatus());

        return savedAppointment;
    }

    public Appointment updateStatus(Long appointmentId, String newStatus) {
        logger.info("Updating status for Appointment ID: {} to {}", appointmentId, newStatus);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    logger.error("Update failed: Appointment ID {} not found.", appointmentId);
                    return new RuntimeException("Resource not found: The specified appointment record does not exist.");
                });

        appointment.setStatus(newStatus);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        logger.info("Appointment ID: {} status updated successfully to {}", appointmentId, newStatus);

        return updatedAppointment;
    }

    public List<Appointment> getAllAppointments() {
        logger.info("Fetching all appointment records from the database.");
        return appointmentRepository.findAll();
    }

    public List<Appointment> getMyAppointments(String username) {
        logger.info("Fetching appointment history for user: {}", username);
        List<Appointment> appointments = appointmentRepository.findByAppliedByUsername(username);
        logger.info("Found {} appointments for user: {}", appointments.size(), username);
        return appointments;
    }
}