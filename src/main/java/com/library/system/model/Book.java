package com.library.system.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a book in the Library Management System.
 * Supports tracking multiple physical copies.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  
    private String title;

    private String author;

 
    private String category;

  
    private int totalCopies;

  
    private int availableCopies;

  
    private boolean available = true;

   
    private String cover;

   
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
}
