//package com.dev.library.controller;
//
//import com.dev.library.controller.BookManagement.BookController;
//import com.dev.library.entity.Book;
//import com.dev.library.entity.BorrowedBooks;
//import com.dev.library.entity.Borrower;
//import com.dev.library.model.BorrowingManagement.BookBorrowResponse;
//import com.dev.library.service.BookManagement.implementation.BookServiceImpl;
//import com.dev.library.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//public class BookControllerTest {
//
//    @InjectMocks
//    private BookServiceImpl bookServiceImpl;
//
//
//    private BookController bookController;
//
//    @InjectMocks
//    private BorrowedBooksServiceImpl borrowedBooksService;
//
//    Book book;
//
//    @Mock
//    private BookDao bookDao;
//
//    @Mock
//    private static BorrowedBooksDao borrowedBooksDao;
//
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        bookServiceImpl = new BookServiceImpl(bookDao, borrowedBooksDao);
//        book = new Book();
//        bookController = new BookController(bookServiceImpl, bookServiceImpl2);
//        book.setName("Title1");
//        book.setAuthor("Author1");
//        book.setCategory("cat1");
//        book.setIsbn("1001");
//    }
//
//    @Test
//    void testGetAllBooks() {
//        List<Book> books = new ArrayList<>();
//        books.add(book);
//        when(bookServiceImpl.getAllBooks()).thenReturn(books);
//
//        ResponseEntity<List<Book>> response = bookController.getAllBooks();
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(books, response.getBody());
//    }
//
//    @Test
//    void testGetBookById() {
//
//        when(bookDao.findByNameAndIsDeleted(anyString(), anyInt())).thenReturn(book);
//
//        ResponseEntity<Book> response = bookController.getBookByName("Title1");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(book, response.getBody());
//    }
//
//
//    @Test
//    void testUpdateBook() {
//
//        when(bookDao.findById(anyLong())).thenReturn(Optional.ofNullable(book));
//        ResponseEntity<Book> responseEntity = bookController.updateBook(Long.valueOf(10000), book);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    void createBook() {
//
//        when(bookDao.save(any())).thenReturn(book);
//        ResponseEntity<Book> responseEntity = bookController.createBook(book);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    void testDeleteBook() {
//        BookBorrowResponse bookBorrowResponse = new BookBorrowResponse();
//        bookBorrowResponse.setId(1L);
//        bookBorrowResponse.setBookName("BOOK1");
//        bookBorrowResponse.setBorrowerName("BORROWER1");
//        bookBorrowResponse.setBorrowerPhone("123456789");
//        bookBorrowResponse.setIssueDate(LocalDate.now());
//        bookBorrowResponse.setReturnDate(LocalDate.now());
//        List<BookBorrowResponse> bookBorrowResponses = new ArrayList<>();
//        bookBorrowResponses.add(bookBorrowResponse);
//        List<BorrowedBooks> borrowedBooksList = new ArrayList<>();
//        Borrower borrower = new Borrower("BORROWER1", "123456789");
//        BorrowedBooks borrowedBooks = new BorrowedBooks();
//        borrowedBooks.setBook(book);
//        borrowedBooks.setBorrower(borrower);
//        borrowedBooks.setIssueDate(LocalDate.now());
//        borrowedBooks.setReturnDate(LocalDate.now());
//
//        borrowedBooksList.add(borrowedBooks);
//
//        when(bookDao.findById(anyLong())).thenReturn(Optional.ofNullable(book));
////        when(borrowedBooksService.getBorrowedBooksByBookId(anyLong())).thenReturn(bookBorrowResponses);
//        doReturn(borrowedBooksList).when(borrowedBooksDao).findByBookId(anyLong());
//        doNothing().when(bookDao).delete(any());
//
//
//        ResponseEntity<Void> responseEntity = bookController.deleteBook(10000L);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        assertThat(responseEntity.getBody()).isNull();
//
//    }
//
//}
