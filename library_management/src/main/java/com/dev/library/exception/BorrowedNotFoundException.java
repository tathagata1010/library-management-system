package com.dev.library.exception;

public class BorrowedNotFoundException extends RuntimeException{
    public BorrowedNotFoundException(String message) {
        super(message);
    }
}
