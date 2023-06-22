package com.dev.library.service.BorrowingManagement;

import com.dev.library.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library.model.BorrowingManagement.UpdateBookBorrowRequest;

import java.math.BigInteger;
import java.util.List;

public interface BorrowedBooksService {
    List<BookBorrowResponse> getAllBooksBorrowed() throws Exception;


    BookBorrowResponse addBookBorrow(BigInteger bookId, BookBorrowRequest bookBorrowRequest);

    BookBorrowResponse updateBorrowed(BigInteger bookId, BigInteger borrowedId, UpdateBookBorrowRequest updateBookBorrowRequest);

    BookBorrowResponse bookLost(BigInteger bookBorrowId,String borrowerAddress);
}
