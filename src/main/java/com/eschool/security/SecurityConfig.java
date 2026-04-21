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
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. Public Endpoints (Sabke liye open)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/quizzes/*/submit/**").permitAll()
                        .requestMatchers("/api/results/student/**").permitAll()

                        // 2. Admin & Teacher specific (Cleaned up)
                        // Note: hasRole("ADMIN") internally checks for "ROLE_ADMIN"
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/attendance/mark/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/quizzes/**").hasAnyRole("ADMIN", "TEACHER")

                        // 3. Appointment Rules (Simplified)
                        .requestMatchers("/api/appointments/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/appointments/book", "/api/appointments/my").hasRole("APPLICANT")

                        // 4. General Authenticated Rules
                        .requestMatchers("/api/attendance/student/**").authenticated()

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // 1. Disable CSRF (Cross-Site Request Forgery) for REST APIs
//                .csrf(csrf -> csrf.disable())
//
//                // 2. Set Session Policy to STATELESS (No Cookies, only JWT)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                // 3. Define Public vs Private Endpoints
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/quizzes/*/submit/**").permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/results/student/**").permitAll()// to see the quiz result
//                        // Only ADMIN can see ALL appointments or update them
//                        .requestMatchers("/api/appointments/admin/**").hasRole("ADMIN")
//                        // In SecurityConfig.java
//                        .requestMatchers("/api/appointments/admin/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/api/appointments/book").hasAuthority("ROLE_APPLICANT")
//                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/api/attendance/mark/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/api/attendance/student/**").authenticated()
//                        .requestMatchers("/api/quizzes/**").hasAnyRole("ADMIN", "TEACHER")
//                        // APPLICANTS can book
//                        .requestMatchers("/api/appointments/book", "/api/appointments/my").hasRole("APPLICANT")
//                        // SecurityFilterChain ke andar check karo
//
//
//                        .anyRequest().authenticated()
//                );
//
//        // 4. Register our JWT Filter before the standard login filter
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

}