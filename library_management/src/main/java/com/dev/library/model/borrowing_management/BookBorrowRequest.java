package com.dev.library.model.borrowing_management;

import java.time.LocalDate;

public class BookBorrowRequest {
    private String borrowerPhone;
    private String walletAddress;
    private String borrowerName;
    private Long bookId;
    private LocalDate returnDate;
    private LocalDate issueDate;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String borrowerAddress) {
        this.walletAddress = borrowerAddress;
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
                "borrowerPhone='" + borrowerPhone + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", borrowerName='" + borrowerName + '\'' +
                ", bookId=" + bookId +
                ", returnDate=" + returnDate +
                ", issueDate=" + issueDate +
                '}';
    }
}
