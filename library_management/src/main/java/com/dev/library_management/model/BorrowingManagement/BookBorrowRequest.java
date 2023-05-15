package com.dev.library_management.model.BorrowingManagement;

import java.time.LocalDate;

public class BookBorrowRequest {
    private String borrowerName;
    private String borrowerPhone;
    private Long bookId;
    private LocalDate returnDate;
    private LocalDate issueDate;

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

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Override
    public String toString() {
        return "BookBorrowRequest{" +
                "borrowerName='" + borrowerName + '\'' +
                ", borrowerPhone='" + borrowerPhone + '\'' +
                ", bookId=" + bookId +
                ", returnDate=" + returnDate +
                ", issueDate=" + issueDate +
                '}';
    }
}
