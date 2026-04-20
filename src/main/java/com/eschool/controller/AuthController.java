package com.eschool.controller;



import com.eschool.dto.AuthResponse;
import com.eschool.dto.LoginRequest;
import com.eschool.dto.RegisterRequest;
import com.eschool.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Public Sign-up for Parents/Applicants
    @PostMapping("/public/signup")
    public String signup(@RequestBody RegisterRequest request) {
        return authService.registerParent(request);
    }
    @PostMapping("/public/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
