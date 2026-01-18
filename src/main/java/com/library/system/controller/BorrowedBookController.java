package com.library.system.controller;

import com.library.system.model.BorrowedBook;
import com.library.system.service.BorrowedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST controller for managing borrowed books in the library.
 */
@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowedBookController {

    private final BorrowedBookService borrowedBookService;

 
    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<?> borrowBook(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestBody BorrowedBook bookData) {
        try {
            BorrowedBook borrowed = borrowedBookService.borrowBook(userId, bookId, bookData);
            return ResponseEntity.ok(borrowed);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

 
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowedBook>> getUserBorrowedBooks(@PathVariable Long userId) {
        List<BorrowedBook> books = borrowedBookService.getBorrowedBooksByUser(userId);
        return ResponseEntity.ok(books);
    }


    @PutMapping("/return/{borrowId}")
    public ResponseEntity<Map<String, String>> returnBook(@PathVariable Long borrowId) {
        try {
            BorrowedBook result = borrowedBookService.returnBook(borrowId);
            Map<String, String> response = new HashMap<>();

            if (result == null) {
                response.put("message", "Borrow record not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if ("Fine".equals(result.getStatus())) {
                response.put("message", "Book returned late. Fine applied.");
            } else if ("Borrow Cancelled".equals(result.getStatus())) {
                response.put("message", "Borrow cancelled due to invalid dates.");
            } else {
                response.put("message", "Book returned successfully.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while returning the book."));
        }
    }


    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @GetMapping("/count/borrowed")
    public ResponseEntity<Long> getBorrowedBookCount() {
        return ResponseEntity.ok(borrowedBookService.getBorrowedBookCount());
    }

  
    @GetMapping("/fine/{borrowId}")
    public ResponseEntity<Map<String, Integer>> getFine(@PathVariable Long borrowId) {
        try {
            BorrowedBook book = borrowedBookService.getBorrowById(borrowId);
            int fine = borrowedBookService.calculateFine(book);
            return ResponseEntity.ok(Map.of("fine", fine));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("fine", 0));
        }
    }

    
    @GetMapping("/can-borrow/{userId}")
    public ResponseEntity<Map<String, Boolean>> canUserBorrow(@PathVariable Long userId) {
        boolean canBorrow = borrowedBookService.canUserBorrow(userId);
        return ResponseEntity.ok(Map.of("canBorrow", canBorrow));
    }

 
    @GetMapping("/fine-status/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFineStatus(@PathVariable Long userId) {
        List<BorrowedBook> borrowedBooks = borrowedBookService.getBorrowedBooksByUser(userId);
        int totalFine = 0;
        boolean hasFine = false;

        for (BorrowedBook book : borrowedBooks) {
            if (!book.isReturned()) {
                int fine = borrowedBookService.calculateFine(book);
                if (fine > 0) {
                    totalFine += fine;
                    hasFine = true;
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("hasFine", hasFine);
        response.put("fineAmount", totalFine);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}/book/{bookId}/already-borrowed")
    public ResponseEntity<Map<String, Boolean>> hasUserAlreadyBorrowed(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        boolean alreadyBorrowed = borrowedBookService.hasUserAlreadyBorrowedBook(userId, bookId);
        return ResponseEntity.ok(Map.of("alreadyBorrowed", alreadyBorrowed));
    }
}
