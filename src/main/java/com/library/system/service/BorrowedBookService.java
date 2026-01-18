package com.library.system.service;

import com.library.system.model.BorrowedBook;
import java.util.List;

public interface BorrowedBookService {

    BorrowedBook borrowBook(Long userId, Long bookId, BorrowedBook bookData);

    BorrowedBook returnBook(Long borrowId);

    boolean canUserBorrow(Long userId);

    List<BorrowedBook> getBorrowedBooksByUser(Long userId);

    BorrowedBook getBorrowById(Long borrowId);

    long getBorrowedBookCount();

    int calculateFine(BorrowedBook borrowedBook);

    int getTotalUnpaidFineForUser(Long userId);

    /**
     * ✅ Checks if the user has already borrowed this book and not yet returned it.
     *
     * userId = the ID of the user
     * bookId = the ID of the book
     * returns true if already borrowed and not returned; false otherwise
     */
    boolean hasUserAlreadyBorrowedBook(Long userId, Long bookId);
}
