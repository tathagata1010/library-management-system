//package com.dev.library.entity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class BorrowerTest {
//
//    private Borrower borrower;
//    private Book book;
//
//    @BeforeEach
//    void setUp() {
//        borrower = new Borrower("John Smith", "555-1234");
//        book = mock(Book.class);
//        when(book.getId()).thenReturn(1L);
//        when(book.getName()).thenReturn("The Great Gatsby");
//        when(book.getAuthor()).thenReturn("F. Scott Fitzgerald");
//        Set<Book> books = new HashSet<>();
//        books.add(book);
//        borrower.setBooks(books);
//    }
//
//    @Test
//    void testBorrowerId() {
//        borrower.setId(123L);
//        assertEquals(123L, borrower.getId());
//    }
//
//    @Test
//    void testBorrowerName() {
//        assertEquals("John Smith", borrower.getName());
//    }
//
//    @Test
//    void testBorrowerPhoneNumber() {
//        assertEquals("555-1234", borrower.getPhoneNumber());
//    }
//
//    @Test
//    void testBorrowerBooks() {
//        assertNotNull(borrower.getBooks());
//        assertEquals(1, borrower.getBooks().size());
//        assertEquals(1L, borrower.getBooks().iterator().next().getId());
//        assertEquals("The Great Gatsby", borrower.getBooks().iterator().next().getName());
//        assertEquals("F. Scott Fitzgerald", borrower.getBooks().iterator().next().getAuthor());
//    }
//}
