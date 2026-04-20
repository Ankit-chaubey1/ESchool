package com.eschool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/user-data")
    public String getSecretData() {
        return "If you can see this, your JWT token is working perfectly!";
    }
}