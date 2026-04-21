//package com.eschool.service;
//
//
//
//import com.eschool.dto.AuthResponse;
//import com.eschool.dto.LoginRequest;
//import com.eschool.dto.RegisterRequest;
//
//import com.eschool.entity.User;
//import com.eschool.enums.Role;
//import com.eschool.repository.UserRepository;
//import com.eschool.security.JwtUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    public AuthResponse login(LoginRequest request) {
//        // 1. Find user
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2. Check password
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        // 3. Generate Token
//        String token = jwtUtils.generateToken(user.getUsername());
//
//        return new AuthResponse(token, user.getRole().name());
//    }
//
//    public String registerParent(RegisterRequest request) {
//        // 1. Check if user exists
//        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
//            return "Error: Username/Email already taken!";
//        }
//
//        // 2. Create new User entity
//        User user = new User();
//        user.setUsername(request.getUsername());
//
//        // 3. HASH the password!
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        user.setFullName(request.getFullName());
//
//        // 4. Force role to APPLICANT (Security measure)
//        user.setRole(Role.ROLE_APPLICANT);
//
//        user.setEnabled(true);
//        userRepository.save(user);
//
//        return "Success: Parent account registered as ROLE_APPLICANT";
//    }
//}

package com.eschool.service;

import com.eschool.dto.AuthResponse;
import com.eschool.dto.LoginRequest;
import com.eschool.dto.RegisterRequest;
import com.eschool.entity.User;
import com.eschool.enums.Role;
import com.eschool.repository.UserRepository;
import com.eschool.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse login(LoginRequest request) {
        logger.info("Authentication attempt for user: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User '{}' not found.", request.getUsername());
                    return new RuntimeException("Authentication failed: Invalid username or password.");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Incorrect password for user '{}'.", request.getUsername());
            throw new RuntimeException("Authentication failed: Invalid username or password.");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        logger.info("Authentication successful. Token generated for user: {}", user.getUsername());

        return new AuthResponse(token, user.getRole().name());
    }

    public String registerParent(RegisterRequest request) {
        logger.info("Attempting to register a new parent account: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Registration failed: Username '{}' is already taken.", request.getUsername());
            return "Error: The provided username/email is already registered in our system.";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.ROLE_APPLICANT);
        user.setEnabled(true);

        userRepository.save(user);
        logger.info("Parent account successfully registered: {}", request.getUsername());

        return "Success: Parent account has been registered with ROLE_APPLICANT privileges.";
    }
}