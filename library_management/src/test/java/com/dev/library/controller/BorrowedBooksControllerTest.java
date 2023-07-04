package com.dev.library.controller;

import com.dev.library.controller.BorrowingManagement.BorrowedBooksController;
import com.dev.library.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library.model.BorrowingManagement.UpdateBookBorrowRequest;
import com.dev.library.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class BorrowedBooksControllerTest {

    @InjectMocks
    private BorrowedBooksServiceImpl borrowedBooksService;

    @Mock
    LibraryContract_updated libraryContract;

    BookBorrowResponse bookBorrowResponse;

    @Mock
    private BorrowedBooksController borrowedBooksController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        libraryContract = mock(LibraryContract_updated.class);
        borrowedBooksService = new BorrowedBooksServiceImpl();
        borrowedBooksService.setLibraryContract(libraryContract);
        borrowedBooksController = new BorrowedBooksController(borrowedBooksService);
    }

    @Test
    void testGetAllBorrowedBooks() throws Exception {

        LibraryContract_updated.BorrowedBook borrowedBook1 = new LibraryContract_updated.BorrowedBook(BigInteger.valueOf(1), BigInteger.valueOf(1L),"Borrower1" ,"Book 1", "0x1234", BigInteger.valueOf(1623877200), BigInteger.valueOf(0L), false, "1234567890");

        LibraryContract_updated.Book book1 = new LibraryContract_updated.Book(BigInteger.valueOf(1L), "Book 1", "Author 1", "Category 1", false);

        List<Object> mockResult = new ArrayList<>();
        mockResult.add(borrowedBook1);
        mockResult.add(book1);

        RemoteFunctionCall<List> getAllBorrowedBooksFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBorrowedBooks()).thenReturn(getAllBorrowedBooksFunction);
        when(getAllBorrowedBooksFunction.send()).thenReturn(mockResult);

        RemoteFunctionCall<LibraryContract_updated.Book> getBookFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getBook(BigInteger.valueOf(1))).thenReturn(getBookFunction);
        when(getBookFunction.send()).thenReturn(book1);

        ResponseEntity<List<BookBorrowResponse>> response = borrowedBooksController.getAllBookReports();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<BookBorrowResponse> borrowedBooks = response.getBody();
        assertEquals(1, borrowedBooks.size());

        BookBorrowResponse borrowedBook = borrowedBooks.get(0);
        assertEquals(BigInteger.valueOf(1), borrowedBook.getId());
        assertEquals(LocalDate.of(2021, 6, 17), borrowedBook.getIssueDate());
        assertEquals("1234567890", borrowedBook.getBorrowerPhone());
        assertEquals("0x1234", borrowedBook.getBorrowerAddress());
        assertEquals("Borrower 1", borrowedBook.getBorrowerName());
        assertEquals("Book 1", borrowedBook.getBookName());
        assertNull(borrowedBook.getReturnDate());
        assertEquals(false, borrowedBook.getLost());

        verify(libraryContract, times(1)).getAllBorrowedBooks();
        verify(libraryContract, times(1)).getBook(BigInteger.valueOf(1));
    }

    @Test
    void testAddBookReport() throws Exception {

        BigInteger bookId = BigInteger.valueOf(1L);
        BookBorrowRequest bookBorrowRequest = new BookBorrowRequest();
        bookBorrowRequest.setBookId(1L);
        bookBorrowRequest.setWalletAddress("0x123");
        bookBorrowRequest.setBorrowerName("Borrower 1");
        bookBorrowRequest.setBorrowerPhone("1234567890");
        bookBorrowRequest.setReturnDate(LocalDate.now());
        bookBorrowRequest.setIssueDate(LocalDate.now());

        BookBorrowResponse expectedResponse = new BookBorrowResponse();

        RemoteFunctionCall<TransactionReceipt> addBorrowBook = mock(RemoteFunctionCall.class);
        when(libraryContract.borrowBook(any(BigInteger.class), anyString(),anyString(), anyString(), any(BigInteger.class))).thenReturn(addBorrowBook);
        TransactionReceipt transactionReceipt=mock(TransactionReceipt.class);
        when(addBorrowBook.send()).thenReturn(transactionReceipt);

        Log mockEvent = mock(Log.class);
        when(mockEvent.getData()).thenReturn("Event Data");
        when(LibraryContract_updated.getBookBorrowedEvents(transactionReceipt)).thenReturn((List<LibraryContract_updated.BookBorrowedEventResponse>) mockEvent);

// Call the method being tested
        ResponseEntity<BookBorrowResponse> responseEntity = borrowedBooksController.addBookReport(bookId, bookBorrowRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());

    }

    @Test
    void testUpdateBookReport() throws Exception {

        BigInteger bookId = BigInteger.valueOf(1L);
        BigInteger borrowId = BigInteger.valueOf(2L);
        LocalDate returnDate = LocalDate.now();
        UpdateBookBorrowRequest updateRequest = new UpdateBookBorrowRequest();
        updateRequest.setReturnDate(returnDate);

        BookBorrowResponse expectedResponse = new BookBorrowResponse();
        expectedResponse.setBorrowerName("Borrower 1");
        expectedResponse.setId(borrowId);
        expectedResponse.setBookName("Book 1");
        expectedResponse.setBorrowerPhone("1234567890");
        expectedResponse.setIssueDate(LocalDate.now());
        expectedResponse.setReturnDate(returnDate);
        expectedResponse.setBorrowerAddress("0x123");

        RemoteFunctionCall<TransactionReceipt> updateBookReportFunction = mock(RemoteFunctionCall.class);
        TransactionReceipt transactionReceipt=mock(TransactionReceipt.class);
        when(libraryContract.returnBook(eq(bookId), eq(borrowId), any())).thenReturn(updateBookReportFunction);
        when(updateBookReportFunction.send()).thenReturn(transactionReceipt);

        Log mockEvent = mock(Log.class);
        when(mockEvent.getData()).thenReturn("Event Data");
        when(LibraryContract_updated.getBookReturnedEvents(transactionReceipt)).thenReturn((List<LibraryContract_updated.BookReturnedEventResponse>) mockEvent);

        ResponseEntity<BookBorrowResponse> responseEntity = borrowedBooksController.updateBookReport(bookId, borrowId, updateRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

}
