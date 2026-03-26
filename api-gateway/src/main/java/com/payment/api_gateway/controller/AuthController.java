package com.payment.api_gateway.controller;


import com.payment.api_gateway.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    public AuthController(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        log.info("Login attempt - username: {}, password: {}", username, password);

        if ("admin".equals(username) && "password123".equals(password)) {
            log.info("Credentials matched, generating token...");
            try {
                String token = jwtUtil.generateToken(username);
                log.info("Token generated: {}", token);
                return ResponseEntity.ok(Map.of("token", token));
            } catch (Exception e) {
                log.error("Token generation failed: {}", e.getMessage(), e);
                return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
            }
        }

        log.info("Credentials did not match");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials"));
    }
}
