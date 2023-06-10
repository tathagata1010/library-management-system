//package com.dev.library.entity;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class BorrowedBooksTest {
//
//    private Book book;
//    private Borrower borrower;
//    private LocalDate issueDate;
//    private LocalDate returnDate;
//    private Boolean isLost;
//
//    private BorrowedBooks borrowedBooks;
//
//    @BeforeEach
//    void setUp() {
//        book = new Book();
//        borrower = new Borrower();
//        issueDate = LocalDate.now();
//        returnDate = LocalDate.now().plusDays(14);
//        isLost = false;
//
//        borrowedBooks = new BorrowedBooks(1L, book, borrower, issueDate, returnDate, isLost);
//    }
//
//    @Test
//    void testGettersAndSetters() {
//        assertEquals(1L, borrowedBooks.getId());
//        assertEquals(book, borrowedBooks.getBook());
//        assertEquals(borrower, borrowedBooks.getBorrower());
//        assertEquals(issueDate, borrowedBooks.getIssueDate());
//        assertEquals(returnDate, borrowedBooks.getReturnDate());
//        assertEquals(isLost, borrowedBooks.getLost());
//
//        LocalDate newIssueDate = LocalDate.now().minusDays(7);
//        borrowedBooks.setIssueDate(newIssueDate);
//        assertEquals(newIssueDate, borrowedBooks.getIssueDate());
//
//        LocalDate newReturnDate = LocalDate.now().plusDays(21);
//        borrowedBooks.setReturnDate(newReturnDate);
//        assertEquals(newReturnDate, borrowedBooks.getReturnDate());
//
//        Boolean newIsLost = true;
//        borrowedBooks.setLost(newIsLost);
//        assertEquals(newIsLost, borrowedBooks.getLost());
//    }
//}
