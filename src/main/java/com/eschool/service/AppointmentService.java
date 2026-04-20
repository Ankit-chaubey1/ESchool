package com.eschool.service;

import com.eschool.entity.Appointment;
import com.eschool.entity.User;
import com.eschool.repository.AppointmentRepository;
import com.eschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public Appointment bookAppointment(Appointment appointment, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        appointment.setAppliedBy(user);
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }
    public Appointment updateStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(newStatus);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    public List<Appointment> getMyAppointments(String username) {
        return appointmentRepository.findByAppliedByUsername(username);
    }
}