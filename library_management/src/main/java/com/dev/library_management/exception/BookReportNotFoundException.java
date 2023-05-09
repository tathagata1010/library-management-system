package com.dev.library_management.exception;

public class BookReportNotFoundException extends RuntimeException{
    public BookReportNotFoundException(String message) {
        super(message);
    }
}
