package com.library.system.controller;

import com.library.system.model.Book;
import com.library.system.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing books in the library system.
 * Provides endpoints for adding, retrieving, and deleting books,
 * as well as category and statistics APIs.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

 
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }


    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

  
    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }


    @GetMapping("/category/{category}")
    public List<Book> getAvailableBooksByCategory(@PathVariable String category) {
        return bookService.getAvailableBooksByCategory(category);
    }


    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }


    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @GetMapping("/count")
    public long getBookCount() {
        return bookService.getBookCount();
    }

  
    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @GetMapping("/count/available")
    public long getAvailableBookCount() {
        return bookService.getAvailableBookCount();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id).orElse(null);	//response kodukkunnathu kondu aanu orElse(null) koduthe
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

   
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = bookService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
