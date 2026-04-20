package com.eschool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery) for REST APIs
                .csrf(csrf -> csrf.disable())

                // 2. Set Session Policy to STATELESS (No Cookies, only JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. Define Public vs Private Endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Only ADMIN can see ALL appointments or update them
                        .requestMatchers("/api/appointments/admin/**").hasRole("ADMIN")
                        // In SecurityConfig.java
                        .requestMatchers("/api/appointments/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/appointments/book").hasAuthority("ROLE_APPLICANT")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        // APPLICANTS can book
                        .requestMatchers("/api/appointments/book", "/api/appointments/my").hasRole("APPLICANT")
                        .anyRequest().authenticated()
                );

        // 4. Register our JWT Filter before the standard login filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}