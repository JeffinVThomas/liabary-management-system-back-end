package com.library.system.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a record of a book borrowed by a user.
 * Includes borrow/return dates, fine status, and return status.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne
    private User user;

 
    @ManyToOne
    private Book book;


    private LocalDate borrowDate;

 
    private LocalDate returnDate;

 
    private boolean finePaid;


    private boolean returned;

   
    private String status;
}
