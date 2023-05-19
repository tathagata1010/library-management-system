package com.dev.library_management.service.BorrowingManagement;

import com.dev.library_management.entity.Borrower;
import com.dev.library_management.exception.BookAlreadyIssuedException;
import com.dev.library_management.exception.BorrowedNotFoundException;
import com.dev.library_management.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library_management.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library_management.model.BorrowingManagement.UpdateBookBorrowRequest;

import java.util.List;

public interface BorrowedBooksService {
    List<BookBorrowResponse> getAllBookReports();

    BookBorrowResponse getBookReportById(Long id) throws BorrowedNotFoundException;



    BookBorrowResponse addBookReport(Long bookId, BookBorrowRequest bookBorrowRequest) throws BookAlreadyIssuedException;

    BookBorrowResponse updateBookReport(Long bookId, Long borrowedId, UpdateBookBorrowRequest updateBookBorrowRequest) throws BorrowedNotFoundException;

    void deleteBookReport(Long id) throws BorrowedNotFoundException;

    List<Borrower> getBorrowersByBookId(Long bookId);
}
