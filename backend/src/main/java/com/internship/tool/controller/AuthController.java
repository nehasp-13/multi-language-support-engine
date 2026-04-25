package com.internship.tool.controller;

import com.internship.tool.config.JwtUtil;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Temporary in-memory store until DB is connected
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody User user) {
        if (userStore.containsKey(user.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userStore.put(user.getEmail(), user);
        log.info("Registered user: {}", user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        User user = userStore.get(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(email, user.getRole());
        log.info("Login successful for: {}", email);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }

    // POST /api/auth/refresh
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ValidationException("Invalid token");
        }
        String oldToken = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(oldToken)) {
            throw new ValidationException("Token is invalid or expired");
        }

        String email = jwtUtil.extractEmail(oldToken);
        String role = jwtUtil.extractRole(oldToken);
        String newToken = jwtUtil.generateToken(email, role);

        Map<String, String> response = new HashMap<>();
        response.put("token", newToken);
        return ResponseEntity.ok(response);
    }
}