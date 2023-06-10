package com.dev.library.model.BorrowingManagement;

import java.time.LocalDate;

public class UpdateBookBorrowRequest {
    private LocalDate returnDate;

    private boolean isLost;

    public LocalDate getReturnDate() {

        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {

        this.returnDate = returnDate;
    }

    public boolean getLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    @Override
    public String toString() {
        return "UpdateBookBorrowRequest{" +
                "returnDate=" + returnDate +
                ", isLost=" + isLost +
                '}';
    }
}
