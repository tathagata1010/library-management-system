package com.dev.library.dto.borrowing_management;

import java.time.LocalDate;
import java.util.StringTokenizer;

public class UpdateBookBorrowRequest {
    private LocalDate returnDate;

    private boolean isLost;

    private String borrowerAddress;

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public String getBorrowerAddress() {
        return borrowerAddress;
    }

    public void setBorrowerAddress(String borrowerAddress) {
        this.borrowerAddress = borrowerAddress;
    }

    @Override
    public String toString() {
        StringTokenizer tokenizer = new StringTokenizer(", ");
        StringBuilder sb;
        sb = new StringBuilder("UpdateBookBorrowRequest{");

        sb.append("returnDate=").append(returnDate).append(tokenizer);
        sb.append("isLost=").append(isLost).append(tokenizer);
        sb.append("borrowerAddress='").append(borrowerAddress).append('\'').append(tokenizer);
        sb.append('}');

        return sb.toString();
    }
}
