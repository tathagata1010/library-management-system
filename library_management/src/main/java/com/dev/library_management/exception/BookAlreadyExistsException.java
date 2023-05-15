package com.dev.library_management.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String s) {
        super(s);
    }
}
