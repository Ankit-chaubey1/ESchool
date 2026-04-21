//package com.eschool.service;
//
//import com.eschool.entity.User;
//import com.eschool.repository.UserRepository;
//import com.eschool.dto.RegisterRequest; // Ensure you have this DTO
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AdminService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public User createUserByAdmin(RegisterRequest request) {
//        // 1. Check if user already exists
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Error: Username is already taken!");
//        }
//
//        // 2. Create new User entity
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setFullName(request.getFullName());
//
//        // 3. Encode the password (VERY IMPORTANT)
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        // 4. Set Role from Request (ROLE_TEACHER, ROLE_STUDENT, etc.)
//        user.setRole(request.getRole());
//        user.setEnabled(true);
//
//        return userRepository.save(user);
//    }
//}

package com.eschool.service;

import com.eschool.entity.User;
import com.eschool.repository.UserRepository;
import com.eschool.dto.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUserByAdmin(RegisterRequest request) {
        logger.info("Attempting to create a new user by Admin. Username: {}, Role: {}", request.getUsername(), request.getRole());

        // 1. Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("User creation failed: Username '{}' is already registered in the system.", request.getUsername());
            throw new RuntimeException("Conflict: A user with the provided username already exists.");
        }

        // 2. Create new User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());

        // 3. Encode the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Set Role from Request
        user.setRole(request.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        logger.info("User successfully created with ID: {} and Role: {}", savedUser.getId(), savedUser.getRole());

        return savedUser;
    }
}