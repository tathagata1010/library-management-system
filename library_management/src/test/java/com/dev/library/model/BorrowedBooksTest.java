package com.dev.library.model;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BorrowedBooksTest {

    @Test
    public void testGetId() {
        BigInteger expected = BigInteger.valueOf(1);
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setId(expected);
        BigInteger actual = borrowedBooks.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBookId() {
        BigInteger expected = BigInteger.valueOf(100);
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setBookId(expected);
        BigInteger actual = borrowedBooks.getBookId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBorrowerId() {
        BigInteger expected = BigInteger.valueOf(200);
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setBorrowerId(expected);
        BigInteger actual = borrowedBooks.getBorrowerId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIssueDate() {
        LocalDate expected = LocalDate.of(2023, 1, 1);
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setIssueDate(expected);
        LocalDate actual = borrowedBooks.getIssueDate();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetReturnDate() {
        LocalDate expected = LocalDate.of(2023, 1, 15);
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setReturnDate(expected);
        LocalDate actual = borrowedBooks.getReturnDate();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLost() {
        Boolean expected = false;
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        borrowedBooks.setLost(expected);
        Boolean actual = borrowedBooks.getLost();
        assertEquals(expected, actual);
    }
}