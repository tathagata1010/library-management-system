package com.dev.library.exception;
public class BookAlreadyIssuedException extends RuntimeException {
    public BookAlreadyIssuedException(String message) {
        super(message);
    }
}
