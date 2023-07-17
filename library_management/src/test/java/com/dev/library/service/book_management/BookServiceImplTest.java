package com.dev.library.service.book_management;

import com.dev.library.config.ApplicationProperties;
import com.dev.library.model.Book;
import com.dev.library.service.book_management.implementation.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookServiceImplTest {


    @InjectMocks
    ApplicationProperties applicationProperties;

    @Mock
    private LibraryContract_updated libraryContract;

    @Mock
    private BookServiceImpl bookService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        libraryContract = mock(LibraryContract_updated.class);
        bookService = new BookServiceImpl(applicationProperties);
        bookService.setLibraryContract(libraryContract);

    }


    @Test
    void testAddBook() throws Exception {

        Book book = new Book();
        book.setName("example Book");
        book.setAuthor("John Doe");
        book.setCategory("Fiction");

        LibraryContract_updated.Book book1 = new LibraryContract_updated.Book(BigInteger.valueOf(1),"ample", book.getAuthor(), book.getCategory(), false);
        List<LibraryContract_updated.Book> books = new ArrayList<>();
        books.add(book1);
        RemoteFunctionCall getAllBooksFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBooks()).thenReturn(getAllBooksFunction);
        when(getAllBooksFunction.send()).thenReturn(books);

        List bookAddedEvents = new ArrayList<>();
        LibraryContract_updated.BookAddedEventResponse bookAddedEventResponse = mock(LibraryContract_updated.BookAddedEventResponse.class);
        bookAddedEventResponse.id = BigInteger.valueOf(1);
        bookAddedEventResponse.name = "ample Book";
        bookAddedEventResponse.author = "John Doe";
        bookAddedEventResponse.category = "Fiction";
        bookAddedEventResponse.isDeleted = false;
        bookAddedEvents.add(bookAddedEventResponse);

        RemoteFunctionCall<TransactionReceipt> addBookFunction = mock(RemoteFunctionCall.class);
        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(libraryContract.addBook(anyString(), anyString(), anyString(), anyBoolean())).thenReturn( addBookFunction);
        System.out.println(addBookFunction);
        when(addBookFunction.send()).thenReturn(transactionReceipt);

        when(LibraryContract_updated.getBookAddedEvents(transactionReceipt)).thenReturn(bookAddedEvents);

        Book addedBook = bookService.addBook(book);

        assertEquals(BigInteger.valueOf(1L), addedBook.getId());
        assertEquals("example Book", addedBook.getName());
        assertEquals("John Doe", addedBook.getAuthor());
        assertEquals("Fiction", addedBook.getCategory());
        assertFalse(addedBook.getIsDeleted());

        verify(libraryContract).addBook("example Book", "John Doe", "Fiction", false);
    }


    @Test
    void testGetAllBooks() throws Exception {

        List<LibraryContract_updated.Book> contractBooks = new ArrayList<>();
        BigInteger bookId = BigInteger.valueOf(1);
        String bookName = "Book 1";
        String bookAuthor = "Author 1";
        String bookCategory = "Category 1";
        boolean isDeleted = false;

        LibraryContract_updated.Book book = new LibraryContract_updated.Book(
                bookId, bookName, bookAuthor, bookCategory, isDeleted
        );
        contractBooks.add(book);

        RemoteFunctionCall getAllBooksFunction = mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBooks()).thenReturn(getAllBooksFunction);
        when(getAllBooksFunction.send()).thenReturn(contractBooks);


        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());

        Book retrievedBook = result.get(0);
        assertEquals(bookId, retrievedBook.getId());
        assertEquals(bookName, retrievedBook.getName());
        assertEquals(bookAuthor, retrievedBook.getAuthor());
        assertEquals(bookCategory, retrievedBook.getCategory());
        assertEquals(isDeleted, retrievedBook.getIsDeleted());
    }

    @Test
    void testGetBookByName() throws Exception {
        BigInteger id = BigInteger.valueOf(1);
        String name = "The Book";
        String author = "John Doe";
        String category = "Fiction";
        boolean isDeleted = false;

        RemoteFunctionCall<LibraryContract_updated.Book> getBookCall = mock(RemoteFunctionCall.class);
        LibraryContract_updated.Book bookData = new LibraryContract_updated.Book(id, name, author, category, isDeleted);
        when(getBookCall.send()).thenReturn(bookData);

        when(libraryContract.getBookByName("The Book")).thenReturn(getBookCall);

        Book result = bookService.getBookByName("The Book");

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(author, result.getAuthor());
        assertEquals(category, result.getCategory());
        assertEquals(isDeleted, result.getIsDeleted());
    }


    @Test
    void testUpdateBook() throws Exception {

        BigInteger id = BigInteger.valueOf(1);
        Book book = new Book();
        book.setName("Updated Book");
        book.setAuthor("John Doe");
        book.setCategory("Fiction");

        RemoteFunctionCall<TransactionReceipt> updateBookFunction = mock(RemoteFunctionCall.class);
        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(libraryContract.updateBook(any(), anyString(),anyString(),anyString())).thenReturn(updateBookFunction);
        when(updateBookFunction.send()).thenReturn(transactionReceipt);

        List<LibraryContract_updated.BookUpdatedEventResponse> bookUpdatedEvents = new ArrayList<>();

        LibraryContract_updated.BookUpdatedEventResponse bookUpdatedEventResponse = mock(LibraryContract_updated.BookUpdatedEventResponse.class);
        bookUpdatedEventResponse.id=id;
        bookUpdatedEventResponse.name=book.getName();
        bookUpdatedEventResponse.author=book.getAuthor();
        bookUpdatedEventResponse.category=book.getCategory();
        bookUpdatedEventResponse.isDeleted=false;

        when(LibraryContract_updated.getBookUpdatedEvents(transactionReceipt)).thenReturn(bookUpdatedEvents);
        bookUpdatedEvents.add(bookUpdatedEventResponse);


        Book updatedBook = bookService.updateBook(id, book);

        assertEquals(id, updatedBook.getId());
        assertEquals("Updated Book", updatedBook.getName());
        assertEquals("John Doe", updatedBook.getAuthor());
        assertEquals("Fiction", updatedBook.getCategory());
    }

    @Test
    void testDeleteBook() throws Exception {
        RemoteFunctionCall<TransactionReceipt> deleteBookFunction = mock(RemoteFunctionCall.class);
        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(deleteBookFunction.send()).thenReturn(transactionReceipt);
        when(libraryContract.deleteBook(BigInteger.valueOf(1))).thenReturn(deleteBookFunction);

        assertDoesNotThrow(() -> bookService.deleteBook(BigInteger.valueOf(1)));
        verify(deleteBookFunction, times(1)).send();
    }

}