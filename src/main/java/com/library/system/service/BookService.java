package com.library.system.service;

import com.library.system.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing books in the Library Management System.
 * Handles operations like adding, retrieving, filtering, and counting books.
 */
public interface BookService {


    Book addBook(Book book);


    void deleteBook(Long id);

 
    Optional<Book> getBookById(Long id);

  
    List<Book> getAllBooks();

  
    List<Book> getAvailableBooks();


    List<Book> getBooksByCategory(String category);


    List<Book> getAvailableBooksByCategory(String category);


    long getBookCount();

   
    long getAvailableBookCount();

    
    List<String> getAllCategories();
}
