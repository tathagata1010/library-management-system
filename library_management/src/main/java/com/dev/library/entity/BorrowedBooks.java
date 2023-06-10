package com.dev.library.entity;

import java.math.BigInteger;
import java.time.LocalDate;

public class BorrowedBooks {

    private BigInteger id;

    private BigInteger bookId;

    private BigInteger borrowerId;

    private LocalDate issueDate;

    private LocalDate returnDate;

    private Boolean isLost;

    // getters and setters

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getBookId() {
        return bookId;
    }

    public void setBookId(BigInteger bookId) {
        this.bookId = bookId;
    }


    public BigInteger getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(BigInteger borrowerId) {
        this.borrowerId = borrowerId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getLost() {
        return isLost;
    }

    public void setLost(Boolean lost) {
        isLost = lost;
    }

    // constructor
    public BorrowedBooks() {
    }


    public BorrowedBooks(BigInteger id, BigInteger bookId, BigInteger borrowerId, LocalDate issueDate, LocalDate returnDate, Boolean isLost) {
        this.id = id;
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.isLost = isLost;
    }



    @Override
    public String toString() {
        return "BorrowedBooks{" +
                "id=" + id +
                ", book=" + bookId +
                ", borrower=" + borrowerId +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                ", isLost=" + isLost +
                '}';
    }
}
