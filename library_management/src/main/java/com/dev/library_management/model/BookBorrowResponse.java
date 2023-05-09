package com.dev.library_management.model;

import java.time.LocalDate;

public class BookBorrowResponse {
    private LocalDate issueDate;
    private String borrowerName;
    private String borrowerPhone;
    private String bookName;
    private LocalDate returnDate;

    public BookBorrowResponse(LocalDate issueDate, String borrowerName, String borrowerPhone, String bookName, LocalDate returnDate) {
        this.issueDate = issueDate;
        this.borrowerName = borrowerName;
        this.borrowerPhone = borrowerPhone;
        this.bookName = bookName;
        this.returnDate = returnDate;
    }

    public BookBorrowResponse() {

    }

    // getters and setters for each field
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
