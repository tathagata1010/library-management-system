package com.dev.library_management.service;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.dao.BorrowedBooksDao;
import com.dev.library_management.dao.BorrowerDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.model.BookBorrowRequest;
import com.dev.library_management.model.BookBorrowResponse;
import com.dev.library_management.model.UpdateBookBorrowRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class BorrowedBookServiceTest {

    @Mock
    BorrowedBooksDao borrowedBooksDao;

    @Mock
    BookDao bookDao;

    @Mock
    private BookBorrowResponse bookBorrowResponse;

    @Mock
    BorrowerDao borrowerDao;

    @Mock
    BorrowedBooks borrowedBooks;

    @Mock
    Book book;

    @Mock
    Borrower borrower;

    @Mock
    BookService bookService;

    @InjectMocks
    BorrowedBooksService borrowedBooksService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowedBooksService = new BorrowedBooksService(borrowedBooksDao, bookService, bookDao, borrowerDao);
        bookBorrowResponse.setId(1L);
        bookBorrowResponse.setBookName("BOOK1");
        bookBorrowResponse.setBorrowerName("BORROWER1");
        bookBorrowResponse.setBorrowerPhone("123456789");
        bookBorrowResponse.setIssueDate(LocalDate.now());
        bookBorrowResponse.setReturnDate(LocalDate.now());
        book = new Book(1L, "BOOK1", "AUTHOR", "CATEGORY1", "110001");
        borrower = new Borrower("BORROWER1", "123456789");
        borrowedBooks = new BorrowedBooks();
        borrowedBooks.setBook(book);
        borrowedBooks.setBorrower(borrower);
        borrowedBooks.setIssueDate(LocalDate.now());
        borrowedBooks.setReturnDate(LocalDate.now());

    }

    @Test
    void getAllBookReports() {
        List<BookBorrowResponse> bookBorrowResponses = new ArrayList<>();
        bookBorrowResponses.add(bookBorrowResponse);
        List<BorrowedBooks> borrowedBooksList = new ArrayList<>();
        borrowedBooksList.add(borrowedBooks);
        BookBorrowResponse expectedResponse = new BookBorrowResponse();
        expectedResponse.setIssueDate(LocalDate.of(2023, 5, 10));
        expectedResponse.setBorrowerName("BORROWER1");
        expectedResponse.setBorrowerPhone("123456789");
        expectedResponse.setBookName("BOOK1");
        expectedResponse.setReturnDate(LocalDate.of(2023, 5, 10));
        doReturn(borrowedBooksList).when(borrowedBooksDao).findAll();
        List<BookBorrowResponse> responses = borrowedBooksService.getAllBookReports();
        assertThat(responses).isNotEmpty();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).toString()).isEqualTo(expectedResponse.toString());
    }

    @Test
    void getBookReportById() {
        doReturn(Optional.of(borrowedBooks)).when(borrowedBooksDao).findById(anyLong());
        BookBorrowResponse expectedResponse = new BookBorrowResponse();

        expectedResponse.setBorrowerName("BORROWER1");
        expectedResponse.setBorrowerPhone("123456789");
        expectedResponse.setIssueDate(LocalDate.now());
        expectedResponse.setBookName("BOOK1");
        expectedResponse.setReturnDate(LocalDate.now());
        BookBorrowResponse responses = borrowedBooksService.getBookReportById(1L);
        assertThat(responses.toString()).isNotNull().isEqualTo(expectedResponse.toString());

    }

    @Test
    void addBookReport(){
        doReturn(Optional.of(book)).when(bookDao).findById(anyLong());
        doReturn(borrower).when(borrowerDao).findByName(anyString());
        doReturn(borrowedBooks).when(borrowedBooksDao).save(any());
        BookBorrowResponse expectedResponse = new BookBorrowResponse();

        expectedResponse.setBorrowerName("BORROWER1");
        expectedResponse.setBorrowerPhone("123456789");
        expectedResponse.setIssueDate(LocalDate.now());
        expectedResponse.setBookName("BOOK1");
        expectedResponse.setReturnDate(LocalDate.now());

        BookBorrowRequest bookBorrowRequest = new BookBorrowRequest();
        bookBorrowRequest.setBookId(1L);
        bookBorrowRequest.setBorrowerName("BORROWER1");
        bookBorrowRequest.setBorrowerPhone("12345678");
        bookBorrowRequest.setReturnDate(LocalDate.now());
        bookBorrowRequest.setIssueDate(LocalDate.now());

        BookBorrowResponse response = borrowedBooksService.addBookReport(bookBorrowRequest);
        assertThat(response.toString()).isNotNull().isEqualTo(expectedResponse.toString());
    }


    @Test
    void updateBookReport(){
        doReturn(Optional.of(borrowedBooks)).when(borrowedBooksDao).findById(anyLong());
        BookBorrowResponse expectedResponse = new BookBorrowResponse();

        expectedResponse.setBorrowerName("BORROWER1");
        expectedResponse.setBorrowerPhone("123456789");
        expectedResponse.setIssueDate(LocalDate.now());
        expectedResponse.setBookName("BOOK1");
        expectedResponse.setReturnDate(LocalDate.now());

        UpdateBookBorrowRequest updateBookBorrowRequest=new UpdateBookBorrowRequest();
        updateBookBorrowRequest.setReturnDate(LocalDate.now());

        BookBorrowResponse response = borrowedBooksService.updateBookReport(1L,updateBookBorrowRequest);

        assertThat(response.toString()).isNotNull().isEqualTo(expectedResponse.toString());
    }

    @Test
    void deleteBookReport(){
        doReturn(Optional.of(borrowedBooks)).when(borrowedBooksDao).findById(anyLong());

        borrowedBooksService.deleteBookReport(1L);
        Mockito.verify(borrowedBooksDao).delete(borrowedBooks);

    }


}
