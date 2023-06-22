package com.dev.library.service.BorrowingManagement;

import com.dev.library.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library.model.BorrowingManagement.UpdateBookBorrowRequest;
import com.dev.library.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BorrowedBooksServiceImplTest {

    @Mock
    private LibraryContract_updated libraryContract;

    @InjectMocks
    private BorrowedBooksServiceImpl borrowedBooksService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        libraryContract = mock(LibraryContract_updated.class);
        borrowedBooksService=new BorrowedBooksServiceImpl();
        borrowedBooksService.setLibraryContract(libraryContract);
    }




    @Test
    void testGetAllBooksBorrowed() throws Exception {
        RemoteFunctionCall<List> getAllBorrowedBooksFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBorrowedBooks()).thenReturn(getAllBorrowedBooksFunction);

        List<LibraryContract_updated.BorrowedBook> borrowedBooksData = new ArrayList<>();
        borrowedBooksData.add(new LibraryContract_updated.BorrowedBook(BigInteger.valueOf(1),BigInteger.valueOf(1), "Borrower 1","Book 1","0x123",BigInteger.valueOf(1624334400), BigInteger.valueOf(1626912000),false,"1234567890"));
        when(getAllBorrowedBooksFunction.send()).thenReturn(borrowedBooksData);

        RemoteFunctionCall<LibraryContract_updated.Book> getBookFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getBook(Mockito.any())).thenReturn(getBookFunction);
        when(getBookFunction.send()).thenReturn(new LibraryContract_updated.Book(BigInteger.valueOf(1), "Book 1", "Author 1", "Category 1", false));

        List<BookBorrowResponse> borrowedBooks = borrowedBooksService.getAllBooksBorrowed();

        BookBorrowResponse borrowedBook = borrowedBooks.get(0);
        assertEquals(BigInteger.valueOf(1), borrowedBook.getId());
        assertEquals(LocalDate.of(2021, 6, 22), borrowedBook.getIssueDate());
        assertEquals("0x123", borrowedBook.getBorrowerAddress());
        assertEquals("Borrower 1", borrowedBook.getBorrowerName());
        assertEquals("1234567890", borrowedBook.getBorrowerPhone());
        assertEquals("Book 1", borrowedBook.getBookName());
        assertEquals(LocalDate.of(2021, 7, 22), borrowedBook.getReturnDate());
        assertFalse(borrowedBook.getLost());

    }

    @Test
    void testAddBookBorrow() throws Exception {
        BigInteger bookId = BigInteger.valueOf(1);
        BookBorrowRequest bookBorrowRequest = new BookBorrowRequest();

        bookBorrowRequest.setBorrowerPhone("1234567890");
        bookBorrowRequest.setBookId(1L);
        bookBorrowRequest.setIssueDate(LocalDate.of(2021, 6, 22));
        bookBorrowRequest.setWalletAddress("0x123");

        RemoteFunctionCall<TransactionReceipt> borrowBookFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.borrowBook(any(BigInteger.class), anyString(),anyString(), anyString(), any(BigInteger.class))).thenReturn(borrowBookFunction);

        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(borrowBookFunction.send()).thenReturn(transactionReceipt);

        List<LibraryContract_updated.BookBorrowedEventResponse> bookBorrowedEvents = new ArrayList<>();
        LibraryContract_updated.BookBorrowedEventResponse bookBorrowedEvent = new LibraryContract_updated.BookBorrowedEventResponse();
        bookBorrowedEvents.add(bookBorrowedEvent);
        when(LibraryContract_updated.getBookBorrowedEvents(transactionReceipt)).thenReturn(bookBorrowedEvents);

        BookBorrowResponse borrowedBookResponse = borrowedBooksService.addBookBorrow(bookId, bookBorrowRequest);

        assertEquals(BigInteger.valueOf(1), borrowedBookResponse.getId());
        assertEquals("Book 1", borrowedBookResponse.getBookName());
        assertEquals("0x123", borrowedBookResponse.getBorrowerAddress());
        assertEquals("1234567890", borrowedBookResponse.getBorrowerPhone());
        assertEquals(LocalDate.of(2021, 6, 22), borrowedBookResponse.getIssueDate());
        assertFalse(borrowedBookResponse.getLost());
   }

    @Test
    void testUpdateBorrowed_ReturnBook() throws Exception {
        BigInteger bookId = BigInteger.valueOf(123);
        BigInteger borrowId = BigInteger.valueOf(456);
        UpdateBookBorrowRequest updateBookBorrowRequest = new UpdateBookBorrowRequest();
        updateBookBorrowRequest.setReturnDate(LocalDate.now());
        updateBookBorrowRequest.setBorrowerAddress("0x123");

        RemoteFunctionCall<TransactionReceipt> returnBookFunctionCall = mock(RemoteFunctionCall.class);
        when(libraryContract.returnBook(any(), any(),anyString())).thenReturn(returnBookFunctionCall);

        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(returnBookFunctionCall.send()).thenReturn(transactionReceipt);

        LibraryContract_updated.BookReturnedEventResponse bookReturnedEvent = mock(LibraryContract_updated.BookReturnedEventResponse.class);
        when(LibraryContract_updated.getBookReturnedEvents(transactionReceipt)).thenReturn(Collections.singletonList(bookReturnedEvent));

        BigInteger returnedBookId = BigInteger.valueOf(123);
        String returnedBookName = "Book Name";
        String returnedBorrowerAddress = "Borrower Address";
        String returnedBorrowerPhone = "Borrower Phone";
        BigInteger returnedIssueDate = BigInteger.valueOf(1623345600); // Example timestamp in seconds
        BigInteger returnedReturnDate = BigInteger.valueOf(1624000799); // Example timestamp in seconds

        BookBorrowResponse updatedBorrowedBook = borrowedBooksService.updateBorrowed(bookId, borrowId, updateBookBorrowRequest);

        verify(libraryContract).returnBook(borrowId, BigInteger.valueOf(updateBookBorrowRequest.getReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()),"0x123");

        verify(returnBookFunctionCall).send();


        assertEquals(returnedBookId, updatedBorrowedBook.getId());
        assertEquals(returnedBookName, updatedBorrowedBook.getBookName());
        assertEquals(returnedBorrowerAddress, updatedBorrowedBook.getBorrowerAddress());
        assertEquals(returnedBorrowerPhone, updatedBorrowedBook.getBorrowerPhone());
        assertEquals(LocalDate.ofEpochDay(returnedIssueDate.longValue()), updatedBorrowedBook.getIssueDate());
        assertEquals(LocalDate.ofEpochDay(returnedReturnDate.longValue()), updatedBorrowedBook.getReturnDate());
        assertFalse(updatedBorrowedBook.getLost());
    }

    @Test
    void testBookLost() throws Exception {

        BigInteger bookBorrowId = BigInteger.valueOf(456);

        RemoteFunctionCall<TransactionReceipt> markBookLost = mock(RemoteFunctionCall.class);
        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);

        List<LibraryContract_updated.BookLostEventResponse> bookLostEvents = Collections.singletonList(
                new LibraryContract_updated.BookLostEventResponse()
        );

        when(libraryContract.reportLost(bookBorrowId,"0x123")).thenReturn(markBookLost);

        when(markBookLost.send()).thenReturn(transactionReceipt);

        Mockito.when(LibraryContract_updated.getBookLostEvents(transactionReceipt))
                .thenReturn(bookLostEvents);

        BookBorrowResponse lostBookResponse = borrowedBooksService.bookLost(bookBorrowId,"0x123");

        assertEquals(bookBorrowId, lostBookResponse.getId());
        assertNull(lostBookResponse.getBookName());
        assertNull(lostBookResponse.getBorrowerAddress());
        assertNull(lostBookResponse.getBorrowerPhone());
        assertNull(lostBookResponse.getIssueDate());
        assertEquals(true, lostBookResponse.getLost());
    }
}