package com.dev.library.model.BorrowingManagement;

import java.time.LocalDate;

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
        return "UpdateBookBorrowRequest{" +
                "returnDate=" + returnDate +
                ", isLost=" + isLost +
                ", borrowerAddress='" + borrowerAddress + '\'' +
                '}';
    }
}
