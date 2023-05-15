package com.dev.library_management.controller;

import com.dev.library_management.controller.BorrowingManagement.BorrowedBooksController;
import com.dev.library_management.dao.BookDao;
import com.dev.library_management.dao.BorrowedBooksDao;
import com.dev.library_management.dao.BorrowerDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library_management.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library_management.model.BorrowingManagement.UpdateBookBorrowRequest;
import com.dev.library_management.service.BookManagement.implementation.BookServiceImpl;
import com.dev.library_management.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BorrowedBooksControllerTest {

    @InjectMocks
    private BorrowedBooksServiceImpl borrowedBooksServiceImpl;

    private BorrowedBooksController borrowedBooksController;

    @Mock
    private BorrowedBooks borrowedBooks;

    @Mock
    private BookBorrowResponse bookBorrowResponse;

    @Mock
    private BorrowedBooksDao borrowedBooksDao;

    @Mock
    private BookDao bookDao;

    @Mock
    private BorrowerDao borrowerDao;

    @Mock
    Borrower borrower;

    @Mock
    Book book;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowedBooksServiceImpl = new BorrowedBooksServiceImpl(borrowedBooksDao, bookServiceImpl, bookDao, borrowerDao);
        borrowedBooksController=new BorrowedBooksController(borrowedBooksServiceImpl);
        bookBorrowResponse.setId(1L);
        bookBorrowResponse.setBookName("BOOK1");
        bookBorrowResponse.setBorrowerName("BORROWER1");
        bookBorrowResponse.setBorrowerPhone("12345678");
        bookBorrowResponse.setIssueDate(LocalDate.now());
        bookBorrowResponse.setReturnDate(LocalDate.now());
    }

    @Test
    void getAllBookReportsTest() {
        // Setup
        List<BookBorrowResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(bookBorrowResponse);

        BorrowedBooksServiceImpl borrowedBooksServiceImplMock = mock(BorrowedBooksServiceImpl.class);
        List<BorrowedBooks> borrowedBooksList=new ArrayList<>();
        borrowedBooks.setBorrower(borrower);
        borrowedBooks.setBook(book);
        borrowedBooks.setReturnDate(LocalDate.now());
        borrowedBooks.setIssueDate(LocalDate.now());
        borrowedBooksList.add(borrowedBooks);
        doReturn(borrowedBooksList).when(borrowedBooksDao).findAll();
        doReturn(expectedResponse).when(borrowedBooksServiceImplMock).getAllBookReports();

        BorrowedBooksController borrowedBooksController = new BorrowedBooksController(borrowedBooksServiceImplMock);

        // Exercise
        ResponseEntity<List<BookBorrowResponse>> response = borrowedBooksController.getAllBookReports();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

//    @Test
//    void getBookReportById(){
//
//        BookBorrowResponse expectedResponse = bookBorrowResponse;
//        BorrowedBooksService borrowedBooksServiceMock = mock(BorrowedBooksService.class);
//        when(borrowedBooksDao.findById(anyLong())).thenReturn(Optional.of(borrowedBooks));
//        doReturn(expectedResponse).when(borrowedBooksServiceMock).getBookReportById(anyLong());
//        BorrowedBooksController borrowedBooksController = new BorrowedBooksController(borrowedBooksServiceMock);
//        ResponseEntity<BookBorrowResponse> response= borrowedBooksController.getBookReportById(1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//    }

    @Test
    void addBookReport(){
        BookBorrowResponse expectedResponse = bookBorrowResponse;
        BorrowedBooksServiceImpl borrowedBooksServiceImplMock = mock(BorrowedBooksServiceImpl.class);
        BookBorrowRequest bookBorrowRequest = new BookBorrowRequest();
        bookBorrowRequest.setBookId(1L);
        bookBorrowRequest.setBorrowerName("BORROWER1");
        bookBorrowRequest.setBorrowerPhone("12345678");
        bookBorrowRequest.setReturnDate(LocalDate.now());
        bookBorrowRequest.setIssueDate(LocalDate.now());
        doReturn(expectedResponse).when(borrowedBooksServiceImplMock).addBookReport(anyLong(),any());

        BorrowedBooksController borrowedBooksController = new BorrowedBooksController(borrowedBooksServiceImplMock);
        ResponseEntity<BookBorrowResponse> response= borrowedBooksController.addBookReport(1L,bookBorrowRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void updateBookReport(){
        BookBorrowResponse expectedResponse = bookBorrowResponse;
        BorrowedBooksServiceImpl borrowedBooksServiceImplMock = mock(BorrowedBooksServiceImpl.class);
        BookBorrowRequest bookBorrowRequest = new BookBorrowRequest();
        UpdateBookBorrowRequest updateBookBorrowRequest=new UpdateBookBorrowRequest();
        updateBookBorrowRequest.setReturnDate(LocalDate.now());
        doReturn(expectedResponse).when(borrowedBooksServiceImplMock).updateBookReport(anyLong(),anyLong(),any());

        BorrowedBooksController borrowedBooksController = new BorrowedBooksController(borrowedBooksServiceImplMock);
        ResponseEntity<BookBorrowResponse> response= borrowedBooksController.updateBookReport(1L,1L,updateBookBorrowRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

//    @Test
//    void deleteBookReport(){
//        BorrowedBooksService borrowedBooksServiceMock = mock(BorrowedBooksService.class);
//        when(borrowedBooksDao.findById(anyLong())).thenReturn(Optional.of(borrowedBooks));
//        doNothing().when(borrowedBooksServiceMock).deleteBookReport(anyLong());
//        ResponseEntity<Void> response= borrowedBooksController.deleteBookReport(1L);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        assertThat(response.getBody()).isNull();
//    }

}
