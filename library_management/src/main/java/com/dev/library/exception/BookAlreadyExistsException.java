package com.dev.library.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String s) {
        super(s);
    }
}
