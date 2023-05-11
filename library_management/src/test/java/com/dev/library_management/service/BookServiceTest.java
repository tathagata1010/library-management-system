package com.dev.library_management.service;
import com.dev.library_management.dao.BookDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookDao bookDao;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setName("Book1");
        book.setAuthor("Author1");
        book.setIsbn("1234567890");
        book.setCategory("Category1");
    }

    @Test
    void testGetAllBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);

        when(bookDao.findAll()).thenReturn(bookList);

        List<Book> result = bookService.getAllBooks();

        assertThat(result).isNotEmpty().containsOnly(book);

        verify(bookDao, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        when(bookDao.findById(anyLong())).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookByName() {
        when(bookDao.findByName(anyString())).thenReturn(book);

        Book result = bookService.getBookByName("Book1");

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).findByName(anyString());
    }


    @Test
    void testAddBook() {
        when(bookDao.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        Book bookDetails = new Book();
        bookDetails.setName("NewBook1");
        bookDetails.setAuthor("NewAuthor1");
        bookDetails.setIsbn("0987654321");
        bookDetails.setCategory("NewCategory1");

        when(bookDao.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookDao.save(any(Book.class))).thenReturn(book);

        Book result = bookService.updateBook(1L, bookDetails);

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).save(any(Book.class));
    }

//    @Test
//
}