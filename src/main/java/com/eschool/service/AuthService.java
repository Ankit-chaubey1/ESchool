package com.eschool.service;



import com.eschool.dto.AuthResponse;
import com.eschool.dto.LoginRequest;
import com.eschool.dto.RegisterRequest;

import com.eschool.entity.User;
import com.eschool.enums.Role;
import com.eschool.repository.UserRepository;
import com.eschool.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse login(LoginRequest request) {
        // 1. Find user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Generate Token
        String token = jwtUtils.generateToken(user.getUsername());

        return new AuthResponse(token, user.getRole().name());
    }

    public String registerParent(RegisterRequest request) {
        // 1. Check if user exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Error: Username/Email already taken!";
        }

        // 2. Create new User entity
        User user = new User();
        user.setUsername(request.getUsername());

        // 3. HASH the password!
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setFullName(request.getFullName());

        // 4. Force role to APPLICANT (Security measure)
        user.setRole(Role.ROLE_APPLICANT);

        user.setEnabled(true);
        userRepository.save(user);

        return "Success: Parent account registered as ROLE_APPLICANT";
    }
}
