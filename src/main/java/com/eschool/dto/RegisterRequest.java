package com.eschool.dto;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    // We don't include 'Role' here for public signup to prevent
    // random people from making themselves ADMINs.
}