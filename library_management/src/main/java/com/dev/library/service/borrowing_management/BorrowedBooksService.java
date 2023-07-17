package com.dev.library.service.borrowing_management;

import com.dev.library.dto.borrowing_management.BookBorrowRequest;
import com.dev.library.dto.borrowing_management.BookBorrowResponse;
import com.dev.library.dto.borrowing_management.UpdateBookBorrowRequest;

import java.math.BigInteger;
import java.util.List;

public interface BorrowedBooksService {
    List<BookBorrowResponse> getAllBooksBorrowed() throws Exception;


    BookBorrowResponse addBookBorrow(BigInteger bookId, BookBorrowRequest bookBorrowRequest);

    BookBorrowResponse updateBorrowed(BigInteger bookId, BigInteger borrowedId, UpdateBookBorrowRequest updateBookBorrowRequest);

    BookBorrowResponse bookLost(BigInteger bookBorrowId,String borrowerAddress);
}
