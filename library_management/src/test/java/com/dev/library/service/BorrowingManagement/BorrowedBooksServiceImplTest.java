package com.dev.library.service.BorrowingManagement;

import com.dev.library.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.web3j.librarycontract.LibraryContract;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class BorrowedBooksServiceImpTest {

    @Mock
    private LibraryContract libraryContract;

    @InjectMocks
    private BorrowedBooksServiceImpl borrowedBooksService;


    @BeforeEach
    void setUp() {

        libraryContract = mock(LibraryContract.class);
        borrowedBooksService=new BorrowedBooksServiceImpl();
        borrowedBooksService.setLibraryContract(libraryContract);
    }


        @Test

        void testGetAllBooksBorrowed() throws Exception {

            RemoteFunctionCall getAllBooksBorrowedFunction = mock(RemoteFunctionCall.class);

            List resultList = new ArrayList<>();

            LibraryContract.BorrowedBookResponse borrowedBookResponseMock = mock(LibraryContract.BorrowedBookResponse.class);
            borrowedBookResponseMock.id = BigInteger.valueOf(1);

            borrowedBookResponseMock.bookName = "Book Name";

            borrowedBookResponseMock.borrowerName = "Borrower Name";

            borrowedBookResponseMock.borrowerPhoneNumber = "1234567890";

            borrowedBookResponseMock.issueDate = BigInteger.valueOf(1623106800);
            borrowedBookResponseMock.returnDate = BigInteger.valueOf(0);
            borrowedBookResponseMock.isLost = false;

            resultList.add(borrowedBookResponseMock);

            when(libraryContract.getBorrowedBooks()).thenReturn(getAllBooksBorrowedFunction);
            when(getAllBooksBorrowedFunction.send()).thenReturn(resultList);

            List<BookBorrowResponse> borrowedBooks = borrowedBooksService.getAllBooksBorrowed();


            // Assert the expected values

            Assertions.assertEquals(1, borrowedBooks.size());
            BookBorrowResponse borrowedBook = borrowedBooks.get(0);
            Assertions.assertEquals(BigInteger.valueOf(1), borrowedBook.getId());
            Assertions.assertEquals(LocalDate.of(2021, 6, 8), borrowedBook.getIssueDate());
            Assertions.assertEquals("Borrower Name", borrowedBook.getBorrowerName());
            Assertions.assertEquals("1234567890", borrowedBook.getBorrowerPhone());
            Assertions.assertEquals("Book Name", borrowedBook.getBookName());
            Assertions.assertNull(borrowedBook.getReturnDate());
            Assertions.assertFalse(borrowedBook.getLost());

        }

    @Test
    public void testBookLost() {
// Arrange
        BigInteger bookBorrowId = BigInteger.valueOf(1);
        LibraryContract.BookLostEventResponse bookLostEvent = mock(LibraryContract.BookLostEventResponse.class);
        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        List<LibraryContract.BookLostEventResponse> bookLostEvents = new ArrayList<>();
        bookLostEvents.add(bookLostEvent);

        when(libraryContract.markBookLost(bookBorrowId)).thenReturn(addBorrowBook);
        when(addBorrowBook.send()).thenReturn(transactionReceipt);
        when(LibraryContract.getBookLostEvents(transactionReceipt)).thenReturn(bookLostEvents);
        when(bookLostEvent.getBorrowedBookId()).thenReturn(bookBorrowId);
        when(bookLostEvent.getBookName()).thenReturn("Book 1");
        when(bookLostEvent.getBorrowerName()).thenReturn("Borrower 1");
        when(bookLostEvent.getBorrowerPhoneNumber()).thenReturn("123456789");

// Act
        BookBorrowResponse result = libraryContract.bookLost(bookBorrowId);

// Assert
        assertEquals(bookBorrowId, result.getId());
        assertEquals("Book 1", result.getBookName());
        assertEquals("Borrower 1", result.getBorrowerName());
        assertEquals("123456789", result.getBorrowerPhone());
        assertTrue(result.isLost());

// Additional assertions or verifications if needed
    }

    }