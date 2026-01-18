package com.library.system.service;

import com.library.system.model.Book;
import com.library.system.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling book-related operations like
 * adding, deleting, fetching, and filtering books by category or availability.
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

  
    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }


    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

 
    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);  
    }

  
    @Override
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }


    @Override
    public List<Book> getAvailableBooksByCategory(String category) {
        return bookRepository.findByCategoryAndAvailableTrue(category);
    }


    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }


    @Override
    public long getBookCount() {
        return bookRepository.count();
    }

 
    @Override
    public long getAvailableBookCount() {
        return bookRepository.findByAvailableTrue().size(); //effective for small dataset
    }

  
    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

 
    @Override
    public List<String> getAllCategories() {
        return bookRepository.findDistinctCategories();
    }
}
