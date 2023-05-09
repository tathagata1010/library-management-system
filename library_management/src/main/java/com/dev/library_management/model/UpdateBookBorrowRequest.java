package com.dev.library_management.model;

import java.time.LocalDate;

public class UpdateBookBorrowRequest {
    private LocalDate returnDate;

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
