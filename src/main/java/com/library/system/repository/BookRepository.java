package com.library.system.repository;

import com.library.system.model.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for Book entity.
 * Provides CRUD operations and custom queries for books.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

 
    List<Book> findByCategory(String category);


    List<Book> findByAvailableTrue();

  
    List<Book> findByCategoryAndAvailableTrue(String category);

    /**
     * Retrieves a distinct list of all book categories in the database.
     *
     * returns a list of unique category names
     */
    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findDistinctCategories();
    
    List<Book> findByAvailableCopiesGreaterThan(int count);

}
