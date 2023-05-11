package com.dev.library_management.exception;

public class BorrowedNotFoundException extends RuntimeException{
    public BorrowedNotFoundException(String message) {
        super(message);
    }
}
