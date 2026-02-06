package com.exemple.eventapi.controller;

import com.exemple.eventapi.service.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if ("admin".equals(username) && "password".equals(password)) {
            String token = jwtService.generateToken(username, "ADMIN");
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Identifiants invalides");
        }
    }
}
