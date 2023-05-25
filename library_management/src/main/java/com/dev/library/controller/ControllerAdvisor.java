package com.dev.library.controller;

import com.dev.library.exception.*;
import com.dev.library.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(BookAlreadyIssuedException.class)
    public ResponseEntity<Map<String, String>> handleBookAlreadyIssuedException(BookAlreadyIssuedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(Constants.ERROR_CODE, "BOOK_ALREADY_ISSUED");
        errorResponse.put(Constants.ERROR_MESSAGE, ex.getMessage());
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookNotFoundException(BookNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(Constants.ERROR_CODE, "BOOK_NOT_FOUND");
        errorResponse.put(Constants.ERROR_MESSAGE, ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BorrowedNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBorrowedNotFoundException(BorrowedNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(Constants.ERROR_CODE, "BORROWED_NOT_FOUND");
        errorResponse.put(Constants.ERROR_MESSAGE, ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(Constants.ERROR_CODE, "BOOK_EXIST");
        errorResponse.put(Constants.ERROR_MESSAGE,  ex.getMessage());
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BookCannotBeDeletedException.class)
    public ResponseEntity<Map<String, String>> handleBookCannotBeDeletedException(BookCannotBeDeletedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(Constants.ERROR_CODE, "BOOK_CANNOT_BE_DELETED");
        errorResponse.put(Constants.ERROR_MESSAGE,  ex.getMessage());
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    

}