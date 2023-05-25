package com.dev.library.exception;

public class BookCannotBeDeletedException extends RuntimeException {
    public BookCannotBeDeletedException(String message) {
        super(message);
    }
}
