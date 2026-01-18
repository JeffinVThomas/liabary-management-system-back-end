package com.library.system.service;

import com.library.system.model.BorrowedBook;
import com.library.system.model.User;
import com.library.system.repository.BorrowedBookRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduler component that runs daily at 10:00 AM
 * to send SMS reminders to users who have borrowed books
 * due in exactly 2 days.
 */
@Component
public class ReminderScheduler {

    private final BorrowedBookRepository borrowedBookRepository;
    private final SmsService smsService;

  
    public ReminderScheduler(BorrowedBookRepository borrowedBookRepository, SmsService smsService) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.smsService = smsService;
    }

    /**
     * Scheduled method that runs every day at 10:00 AM.
     * It finds all borrowed books that are due in 2 days and not yet returned,
     * and sends reminder messages to their respective users.
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendReminders() {
        LocalDate twoDaysLater = LocalDate.now().plusDays(2);

        // Get all books that are due in exactly 2 days and are still not returned
        List<BorrowedBook> dueSoonList = borrowedBookRepository.findByReturnDateAndReturned(twoDaysLater, false);

        for (int i = 0; i < dueSoonList.size(); i++) {
            BorrowedBook book = dueSoonList.get(i);
            User user = book.getUser();

            String mobile = user.getMobile();
            String title = book.getBook().getTitle();
            LocalDate returnDate = book.getReturnDate();

            String message = "Reminder: Only 2 days left to return \"" + title + "\" (Due: " + returnDate + ").";

            smsService.sendMessage(mobile, message);
            System.out.println("Reminder sent to " + mobile + " for book: " + title);
        }
    }
}
