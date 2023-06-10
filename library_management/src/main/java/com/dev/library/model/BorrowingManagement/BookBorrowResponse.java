package com.dev.library.model.BorrowingManagement;

import java.math.BigInteger;
import java.time.LocalDate;

public class BookBorrowResponse {

    private BigInteger id;
    private LocalDate issueDate;
    private String borrowerAddress;
    private String borrowerPhone;
    private String bookName;
    private LocalDate returnDate;
    private Boolean isLost;

    public BookBorrowResponse(BigInteger id, LocalDate issueDate, String borrowerAddress, String borrowerPhone, String bookName, LocalDate returnDate, Boolean isLost) {
        this.id = id;
        this.issueDate = issueDate;
        this.borrowerAddress = borrowerAddress;
        this.borrowerPhone = borrowerPhone;
        this.bookName = bookName;
        this.returnDate = returnDate;
        this.isLost = isLost;
    }

    public BookBorrowResponse() {

    }

    // getters and setters for each field


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getBorrowerAddress() {
        return borrowerAddress;
    }

    public void setBorrowerAddress(String borrowerAddress) {
        this.borrowerAddress = borrowerAddress;
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


    public Boolean getLost() {
        return isLost;
    }

    public void setLost(Boolean lost) {
        isLost = lost;
    }


    @Override
    public String toString() {
        return "BookBorrowResponse{" +
                "id=" + id +
                ", issueDate=" + issueDate +
                ", borrowerName='" + borrowerAddress + '\'' +
                ", borrowerPhone='" + borrowerPhone + '\'' +
                ", bookName='" + bookName + '\'' +
                ", returnDate=" + returnDate +
                ", isLost=" + isLost +
                '}';
    }
}
