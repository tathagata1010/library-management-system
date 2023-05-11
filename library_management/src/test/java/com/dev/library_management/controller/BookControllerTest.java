package com.dev.library_management.controller;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class BookControllerTest {

    @InjectMocks
    private BookService bookService;

    private BookController bookController;

    Book book;

    @Mock
    private BookDao bookDao;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookDao);
        book = new Book();
        bookController = new BookController(bookService);
        book.setName("Title1");
        book.setAuthor("Author1");
        book.setCategory("cat1");
        book.setIsbn("1001");
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void testGetBookById() {

        when(bookDao.findByName(anyString())).thenReturn(book);

        ResponseEntity<Book> response = bookController.getBookByName("Title1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }


    @Test
    void testUpdateBook() {

//        when(bookService.updateBook(anyLong(),book)).thenReturn(book);
        when(bookDao.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        ResponseEntity<Book> responseEntity = bookController.updateBook(Long.valueOf(10000), book);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createBook() {

        when(bookDao.save(any())).thenReturn(book);
        ResponseEntity<Book> responseEntity = bookController.createBook(book);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testDeleteBook() {
        when(bookDao.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        doNothing().when(bookDao).delete(any());


        ResponseEntity<Void> responseEntity = bookController.deleteBook(10000L);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();

    }

}
