package com.dev.library_management.service.BookManagement;
import com.dev.library_management.dao.BookDao;
import com.dev.library_management.dao.BorrowedBooksDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library_management.service.BookManagement.implementation.BookServiceImpl;
import com.dev.library_management.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookDao bookDao;

    @Mock
    private BorrowedBooksDao borrowedBooksDao;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

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
        book.setIsDeleted(0);
    }

    @Test
    void testGetAllBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);

        when(bookDao.findAllInAscendingOrderById()).thenReturn(bookList);

        List<Book> result = bookServiceImpl.getAllBooks();
        assertThat(result).isNotEmpty().containsOnly(book);

        verify(bookDao, times(1)).findAllInAscendingOrderById();
    }

    @Test
    void testGetBookById() {
        when(bookDao.findById(anyLong())).thenReturn(Optional.of(book));

        Book result = bookServiceImpl.getBookById(1L);

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookByName() {
        when(bookDao.findByNameAndIsDeleted(anyString(),anyInt())).thenReturn(book);

        Book result = bookServiceImpl.getBookByName("Book1");

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).findByNameAndIsDeleted(anyString(),anyInt());
    }


    @Test
    void testAddBook() {
        when(bookDao.save(any(Book.class))).thenReturn(book);

        Book result = bookServiceImpl.addBook(book);

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
        bookDetails.setIsDeleted(0);

        when(bookDao.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookDao.save(any(Book.class))).thenReturn(book);

        Book result = bookServiceImpl.updateBook(1L, bookDetails);

        assertThat(result).isNotNull().isEqualTo(book);

        verify(bookDao, times(1)).save(any(Book.class));
    }

//    @Test
//    void testDeleteBook(){
//        List<BookBorrowResponse> bookBorrowResponses = new ArrayList<>();
//        BookBorrowResponse bookBorrowResponse=new BookBorrowResponse();
//
//        List<BorrowedBooks> borrowedBooks = new ArrayList<>();
//
//        Borrower  borrower = new Borrower("BORROWER1", "123456789");
//        BorrowedBooks borrowedBook = new BorrowedBooks();
//        borrowedBook.setBook(book);
//        borrowedBook.setBorrower(borrower);
//        borrowedBook.setIssueDate(LocalDate.now());
//        borrowedBook.setReturnDate(LocalDate.now());
//
//        borrowedBooks.add(borrowedBook);
//
//
//        bookBorrowResponse.setId(1L);
//        bookBorrowResponse.setBookName("BOOK1");
//        bookBorrowResponse.setBorrowerName("BORROWER1");
//        bookBorrowResponse.setBorrowerPhone("123456789");
//        bookBorrowResponse.setIssueDate(LocalDate.now());
//        bookBorrowResponse.setReturnDate(LocalDate.now());
//        bookBorrowResponses.add(bookBorrowResponse);
//        when(borrowedBooksDao.findByBookId(anyLong())).thenReturn(borrowedBooks);
//        when(bookDao.findById(anyLong())).thenReturn(Optional.of(book));
//        when(bookDao.save(any(Book.class))).thenReturn(book);
////        when(BorrowedBooksServiceImpl.getBorrowedBooksByBookId(anyLong())).thenReturn(bookBorrowResponses);
//
//        doReturn(bookBorrowResponses).when(borrowedBooksDao).findByBookId(anyLong());
//        bookServiceImpl.deleteBook(1L);
//        verify(bookDao, times(1)).save(any(Book.class));
//    }

}