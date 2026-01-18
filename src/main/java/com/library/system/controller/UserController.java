package com.library.system.controller;

import com.library.system.dto.JwtAuthResponse;
import com.library.system.model.User;
import com.library.system.service.SmsService;
import com.library.system.service.UserService;
import com.library.system.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for handling user-related operations like registration, login, password reset,
 * OTP verification, and JWT-secured endpoints.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        JwtAuthResponse response = new JwtAuthResponse(token, user.getEmail(), user.getRole(), user.getId());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody User loginRequest) {
        User user = userService.loginWithRole(loginRequest.getEmail(), loginRequest.getPassword(), "admin");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        JwtAuthResponse response = new JwtAuthResponse(token, user.getEmail(), user.getRole(), user.getId());
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);	//to remove bearer
            boolean isValid = jwtUtil.validateToken(token);
            if (isValid) {
                String email = jwtUtil.getEmailFromToken(token);	//extract email from tocken
                Optional<User> userOpt = userService.findByEmail(email);
                if (userOpt.isPresent()) {
                    return ResponseEntity.ok(userOpt.get());	//returns userdata
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }
    

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok("Token is valid");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String mobile) {
        try {
            userService.sendOtp(mobile);
            return ResponseEntity.ok("OTP sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestParam String mobile, @RequestParam String otp) {
        boolean isValid = userService.verifyOtp(mobile, otp);
        return ResponseEntity.ok(isValid);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String mobile, @RequestParam String newPassword) {
        Optional<User> userOpt = userService.findByMobile(mobile);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userService.updatePassword(user.getEmail(), newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile number not found");
        }
    }


    @GetMapping("/by-mobile")
    public ResponseEntity<User> getUserByMobile(@RequestParam String mobile) {
        Optional<User> userOpt = userService.findByMobile(mobile);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    @GetMapping("/admin-exists")
    public ResponseEntity<Boolean> checkAdminExists() {
        boolean exists = userService.existsByRole("admin");
        return ResponseEntity.ok(exists);
    }
}
