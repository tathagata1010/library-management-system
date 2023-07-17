package com.dev.library.controller;

import com.dev.library.config.ApplicationProperties;
import com.dev.library.controller.book_management.BookController;
import com.dev.library.model.Book;
import com.dev.library.exception.BookCannotBeDeletedException;
import com.dev.library.exception.BookNotFoundException;
import com.dev.library.service.book_management.implementation.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

 class BookControllerTest {

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

     @InjectMocks
     ApplicationProperties applicationProperties;

    @Mock
    LibraryContract_updated libraryContract;


    private BookController bookController;


    Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        libraryContract = mock(LibraryContract_updated.class);
        bookServiceImpl = new BookServiceImpl(applicationProperties);
        bookServiceImpl.setLibraryContract(libraryContract);
        book = new Book();
        bookController = new BookController(bookServiceImpl);
        book.setName("Title1");
        book.setAuthor("Author1");
        book.setCategory("cat1");
    }


    @Test
    void testGetAllBooks() throws Exception {

        List<LibraryContract_updated.Book> result = new ArrayList<>();
        result.add(new LibraryContract_updated.Book(
                BigInteger.valueOf(1),
                "book1",
                "auth1",
                "cat1",
                false
        ));
        result.add(new LibraryContract_updated.Book(
                BigInteger.valueOf(2),
                "book2",
                "auth2",
                "cat2",
                false
        ));
        RemoteFunctionCall<List> getAllBooksFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBooks()).thenReturn(getAllBooksFunction);
        when(getAllBooksFunction.send()).thenReturn(result);

        ResponseEntity<List<Book>> booksResponse = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, booksResponse.getStatusCode());
        List<Book> books=booksResponse.getBody();
        assertNotNull(books);
        assertEquals(2, books.size());

        Book book1 = books.get(0);
        assertEquals(BigInteger.valueOf(1), book1.getId());
        assertEquals("book1", book1.getName());
        assertEquals("auth1", book1.getAuthor());
        assertEquals("cat1", book1.getCategory());
        assertFalse(book1.getIsDeleted());

        Book book2 = books.get(1);
        assertEquals(BigInteger.valueOf(2), book2.getId());
        assertEquals("book2", book2.getName());
        assertEquals("auth2", book2.getAuthor());
        assertEquals("cat2", book2.getCategory());
        assertFalse(book2.getIsDeleted());
        verify(getAllBooksFunction, times(1)).send();
    }


    @Test
    void testGetBookByName() throws Exception {

        String bookName = "book 1";

        LibraryContract_updated.Book bookFromWrapper = new LibraryContract_updated.Book(
                BigInteger.valueOf(3),
                "book 1",
                "auth",
                "cat",
                false
        );
        RemoteFunctionCall<LibraryContract_updated.Book> getBookByNameFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getBookByName(bookName)).thenReturn(getBookByNameFunction);
        when(getBookByNameFunction.send()).thenReturn(bookFromWrapper);

        ResponseEntity<Book> bookResponse=bookController.getBookByName(bookName);

        Book book= bookResponse.getBody();

        assertNotNull(book);
        assertEquals(BigInteger.valueOf(3), book.getId());
        assertEquals("book 1", book.getName());
        assertEquals("auth", book.getAuthor());
        assertEquals("cat", book.getCategory());
        assertFalse(book.getIsDeleted());
        verify(getBookByNameFunction, times(1)).send();
    }


    @Test
    void testAddBook() throws Exception {

        List<LibraryContract_updated.Book> result = new ArrayList<>();
        result.add(new LibraryContract_updated.Book(
                BigInteger.valueOf(1),
                "book1",
                "auth1",
                "cat1",
                false
        ));


        Book bookRequest = new Book();
        bookRequest.setName("book 1");
        bookRequest.setAuthor("author");
        bookRequest.setCategory("cat");
        bookRequest.setIsDeleted(false);

        RemoteFunctionCall<List> getAllBooksFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBooks()).thenReturn(getAllBooksFunction);
        when(getAllBooksFunction.send()).thenReturn(result);


        RemoteFunctionCall<TransactionReceipt> addBookFunction = mock(RemoteFunctionCall.class);

        when(libraryContract.addBook(
                anyString(),
                anyString(),
                anyString(),
                anyBoolean()
        )).thenReturn(addBookFunction);

        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(addBookFunction.send()).thenReturn(transactionReceipt);

        ResponseEntity<Book> bookResponse= bookController.createBook(bookRequest);

        assertNotNull(bookResponse);
        Book book=bookResponse.getBody();
        assert book != null;
        assertEquals(BigInteger.valueOf(3), book.getId());
        assertEquals("book 3", book.getName());
        assertEquals("auth", book.getAuthor());
        assertEquals("cat", book.getCategory());
    }


    @Test
    void testUpdateBook() throws Exception {

        BigInteger id = BigInteger.valueOf(3);
        Book book = new Book();
        book.setId(id);
        book.setName("book 3");
        book.setAuthor("auth");
        book.setCategory("cat");

        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        RemoteFunctionCall<TransactionReceipt> updateBookFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.updateBook(any(), anyString(), anyString(), anyString())).thenReturn(updateBookFunction);
        when(updateBookFunction.send()).thenReturn(transactionReceipt);


        ResponseEntity<Book> responseEntity = bookController.updateBook(id, book);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
    }


    @Test
    void testDeleteBook() throws BookNotFoundException, BookCannotBeDeletedException {

        BigInteger id = BigInteger.valueOf(3);


        RemoteFunctionCall<TransactionReceipt> deleteBookFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.deleteBook(any())).thenReturn(deleteBookFunction);

        ResponseEntity<Void> responseEntity = bookController.deleteBook(id);


        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }


}
