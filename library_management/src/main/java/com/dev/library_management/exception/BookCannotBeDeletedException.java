package com.dev.library_management.exception;

public class BookCannotBeDeletedException extends RuntimeException {
    public BookCannotBeDeletedException(String message) {
        super(message);
    }
}
