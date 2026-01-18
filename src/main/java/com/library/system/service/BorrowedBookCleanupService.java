package com.library.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.library.system.model.BorrowedBook;
import com.library.system.repository.BorrowedBookRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for cleaning up returned borrowed book records
 * that are older than a specific number of days (e.g., 2 days).
 *
 * This helps keep the database clean by removing unnecessary old records.
 */
@Service
public class BorrowedBookCleanupService {

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    /**
     * Deletes all returned borrowed book records that were returned
     * more than 2 days ago.
     *
     * This method runs automatically every day at midnight (00:00 AM).
     * The schedule is defined using a cron expression.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at 00:00 AM
    public void deleteOldReturnedBooks() {
        LocalDate cutoffDate = LocalDate.now().minusDays(2);

        // Fetch all books that are returned and whose return date is older than cutoff
        List<BorrowedBook> oldReturnedBooks = borrowedBookRepository
            .findByReturnedTrueAndReturnDateBefore(cutoffDate);

        // If any such books are found, delete them
        if (oldReturnedBooks != null && !oldReturnedBooks.isEmpty()) {
            borrowedBookRepository.deleteAll(oldReturnedBooks);
            System.out.println("✅ Deleted returned books older than 2 days: " + oldReturnedBooks.size());
        } else {
            System.out.println("ℹ️ No returned books found older than 2 days.");
        }
    }
}
