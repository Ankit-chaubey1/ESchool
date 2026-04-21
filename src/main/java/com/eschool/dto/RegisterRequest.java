package com.eschool.dto;
import com.eschool.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private Role role;
    // We don't include 'Role' here for public signup to prevent
    // random people from making themselves ADMINs.
}