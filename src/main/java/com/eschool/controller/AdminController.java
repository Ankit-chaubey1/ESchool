package com.eschool.controller;

import com.eschool.entity.User;
import com.eschool.service.AdminService;
import com.eschool.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Is API ko sirf Admin hit kar sakta hai
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createNewUser(@RequestBody RegisterRequest request) {
        try {
            User createdUser = adminService.createUserByAdmin(request);
            return ResponseEntity.ok("User created successfully with role: " + createdUser.getRole());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}