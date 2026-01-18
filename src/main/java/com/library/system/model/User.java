package com.library.system.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user in the Library Management System.
 * A user can be a normal user or an admin.
 * Supports OTP-based login and password reset.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  
    private String name;

  
    @Column(unique = true)
    private String email;


    private String password;


    private String role = "user";

 
    @Column(length = 10, unique = true)
    private String mobile;


    private String otp;

   
    @Column(name = "otp_generated_time", columnDefinition = "DATETIME")
    private LocalDateTime otpGeneratedTime;
}
