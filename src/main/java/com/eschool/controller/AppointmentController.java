package com.eschool.controller;

import com.eschool.entity.Appointment;
import com.eschool.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public Appointment book(@RequestBody Appointment appointment, Authentication auth) {
        // auth.getName() gives us the username stored in the JWT!
        return appointmentService.bookAppointment(appointment, auth.getName());
    }

    @GetMapping("/my")
    public List<Appointment> getMy(Authentication auth) {
        return appointmentService.getMyAppointments(auth.getName());
    }
    // Admin sees everything
    @GetMapping("/admin/all")
    public List<Appointment> getAll() {
        return appointmentService.getAllAppointments();
    }

    // Admin approves/rejects
    @PutMapping("/admin/approve/{id}")
    public Appointment approve(@PathVariable Long id, @RequestParam String status) {
        return appointmentService.updateStatus(id, status);
    }
}