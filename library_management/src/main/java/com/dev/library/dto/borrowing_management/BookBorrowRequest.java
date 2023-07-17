package com.dev.library.dto.borrowing_management;

import java.time.LocalDate;
import java.util.StringTokenizer;

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
        StringTokenizer tokenizer = new StringTokenizer(", ");
        StringBuilder sb;
        sb = new StringBuilder("BookBorrowRequest{");

        sb.append("borrowerPhone='").append(borrowerPhone).append('\'').append(tokenizer);
        sb.append("walletAddress='").append(walletAddress).append('\'').append(tokenizer);
        sb.append("borrowerName='").append(borrowerName).append('\'').append(tokenizer);
        sb.append("bookId=").append(bookId).append(tokenizer);
        sb.append("returnDate=").append(returnDate).append(tokenizer);
        sb.append("issueDate=").append(issueDate).append(tokenizer);
        sb.append('}');

        return sb.toString();
    }
}
