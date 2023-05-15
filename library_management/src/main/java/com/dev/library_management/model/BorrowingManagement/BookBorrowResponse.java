package com.dev.library_management.model.BorrowingManagement;

import java.time.LocalDate;

public class BookBorrowResponse {

    private Long id;
    private LocalDate issueDate;
    private String borrowerName;
    private String borrowerPhone;
    private String bookName;
    private LocalDate returnDate;
    private Boolean isLost;

    public BookBorrowResponse(Long id, LocalDate issueDate, String borrowerName, String borrowerPhone, String bookName, LocalDate returnDate, Boolean isLost) {
        this.id = id;
        this.issueDate = issueDate;
        this.borrowerName = borrowerName;
        this.borrowerPhone = borrowerPhone;
        this.bookName = bookName;
        this.returnDate = returnDate;
        this.isLost = isLost;
    }

    public BookBorrowResponse() {

    }

    // getters and setters for each field


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
                ", borrowerName='" + borrowerName + '\'' +
                ", borrowerPhone='" + borrowerPhone + '\'' +
                ", bookName='" + bookName + '\'' +
                ", returnDate=" + returnDate +
                ", isLost=" + isLost +
                '}';
    }
}
