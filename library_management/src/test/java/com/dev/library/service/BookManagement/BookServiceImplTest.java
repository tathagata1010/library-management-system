package com.dev.library.service.BookManagement;

import com.dev.library.entity.Book;
import com.dev.library.service.BookManagement.implementation.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.web3j.librarycontract.LibraryContract;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class BookServiceImplTest {


    @Mock
    private LibraryContract libraryContract;

    @Mock
    private BookServiceImpl bookService;


    @BeforeEach
    void setUp() {

        libraryContract = mock(LibraryContract.class);
        bookService=new BookServiceImpl();
        bookService.setLibraryContract(libraryContract);

    }


    @Test
    public void testAddBook() throws Exception {
// Create a book object
        Book book = new Book();
        book.setName("The Book 1");
        book.setAuthor("John Doe");
        book.setCategory("Fiction");
        book.setIsbn("1234567890");

        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
// Mock the result of getAllBooksFunction.send()
        List<LibraryContract.Book> mockedResult = new ArrayList<>();
        LibraryContract.Book book1 = new LibraryContract.Book(BigInteger.valueOf(1), "The Book", "John Doe", "Fiction", "123456789", false);
        mockedResult.add(book1);


//        LibraryContract libraryContractSpy = mock(libraryContract);
        List<Contract.EventValuesWithLog> valueList = new ArrayList<>();
        Contract.EventValuesWithLog eventValues = mock(Contract.EventValuesWithLog.class);

        valueList.add(eventValues);
        doReturn(valueList).when(libraryContract).getBookAddedEvents(transactionReceipt);

// Mock the book details in the eventValues
//        BigInteger id = BigInteger.valueOf(1);
//        String name = "The Book";
//        String author = "John Doe";
//        String category = "Fiction";
//        String isbn = "1234567890";
//        boolean isDeleted = false;
//        when(eventValues.getIndexedValues().get(0).getValue()).thenReturn(id);
//        when(eventValues.getNonIndexedValues().get(0).getValue()).thenReturn(name);
//        when(eventValues.getNonIndexedValues().get(1).getValue()).thenReturn(author);
//        when(eventValues.getNonIndexedValues().get(2).getValue()).thenReturn(category);
//        when(eventValues.getNonIndexedValues().get(3).getValue()).thenReturn(isbn);
//        when(eventValues.getNonIndexedValues().get(4).getValue()).thenReturn(isDeleted);


        // Mock the result of addBook function call
        RemoteFunctionCall<TransactionReceipt> addBookFunction = mock(RemoteFunctionCall.class);
//        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(addBookFunction.send()).thenReturn(transactionReceipt);

// Mock the libraryContract.addBook method to return the addBookFunction
        when(libraryContract.addBook(any(), any(), any(), any(), anyBoolean()))
                .thenReturn(addBookFunction);


        Book responseBook = bookService.addBook(book);

// Verify the expected values in the response book
        assertEquals(book.getName(), responseBook.getName());
        assertEquals(book.getAuthor(), responseBook.getAuthor());
        assertEquals(book.getCategory(), responseBook.getCategory());
        assertEquals(book.getIsbn(), responseBook.getIsbn());
        assertEquals(false, responseBook.getIsDeleted());
    }

    @Test
    void testGetAllBooks() throws Exception {
// Mock the result of getAllBooksFunction.send()
        List<LibraryContract.Book> mockedResult = new ArrayList<>();
        LibraryContract.Book book1 = new LibraryContract.Book(BigInteger.valueOf(1), "Book 1", "Author 1", "Category 1", "ISBN 1", false);
        mockedResult.add(book1);

// Mock libraryContract.getAllBooks() to return getAllBooksFunction
        RemoteFunctionCall<List> getAllBooksFunction = Mockito.mock(RemoteFunctionCall.class);
        when(libraryContract.getAllBooks()).thenReturn(getAllBooksFunction);

// Mock getAllBooksFunction.send() to return the mockedResult
        when(getAllBooksFunction.send()).thenReturn(mockedResult);

        List<Book> books = bookService.getAllBooks();

// Verify that libraryContract.getAllBooks() is called
        Mockito.verify(libraryContract).getAllBooks();

// Verify that getAllBooksFunction.send() is called
        Mockito.verify(getAllBooksFunction).send();

// Verify the expected books are returned
        assertEquals(1, books.size());
        Book expectedBook = new Book(BigInteger.valueOf(1), "Book 1", "Author 1", "Category 1", "ISBN 1", false);
        assertEquals(expectedBook.toString(), books.get(0).toString());
    }

    @Test
    void testGetBookByName() throws Exception {
        BigInteger id = BigInteger.valueOf(1);
        String name = "The Book";
        String author = "John Doe";
        String category = "Fiction";
        String isbn = "1234567890";
        boolean isDeleted = false;

        RemoteFunctionCall<LibraryContract.Book> getBookCall = mock(RemoteFunctionCall.class);
        LibraryContract.Book bookData = new LibraryContract.Book(id, name, author, category, isbn, isDeleted);
        when(getBookCall.send()).thenReturn(bookData);

        when(libraryContract.getBookByName("The Book")).thenReturn(getBookCall);

        Book result = bookService.getBookByName("The Book");

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(author, result.getAuthor());
        assertEquals(category, result.getCategory());
        assertEquals(isbn, result.getIsbn());
        assertEquals(isDeleted, result.getIsDeleted());
    }

    @Test
    public void testUpdateBook() throws Exception {

        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
// Create a mock event object with the required values
        LibraryContract.BookUpdatedEventResponse bookUpdatedEventResponseMock = Mockito.mock(LibraryContract.BookUpdatedEventResponse.class);
        bookUpdatedEventResponseMock.id = BigInteger.valueOf(123);
        bookUpdatedEventResponseMock.name = "Book Name";
        bookUpdatedEventResponseMock.author = "Author";
        bookUpdatedEventResponseMock.category = "Category";
        bookUpdatedEventResponseMock.isbn = "ISBN";
        bookUpdatedEventResponseMock.isDeleted = false;

// Create a list with the mock event object
        List<LibraryContract.BookUpdatedEventResponse> bookUpdatedEventList = new ArrayList<>();
        bookUpdatedEventList.add(bookUpdatedEventResponseMock);
// Configure the getBookUpdatedEvents method to return the mock event list
        Mockito.when(LibraryContract.getBookUpdatedEvents(transactionReceipt))
                .thenReturn(bookUpdatedEventList);


// Mock the necessary function calls
        RemoteFunctionCall<TransactionReceipt> updateBookFunctionMock = mock(RemoteFunctionCall.class);
        when(updateBookFunctionMock.send()).thenReturn(transactionReceipt);

        when(libraryContract.updateBook(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(updateBookFunctionMock);



// Create a sample Book object to pass to the function
        Book book = new Book();
        book.setName("Book Name");
        book.setAuthor("Author");
        book.setCategory("Category");

// Call the function
        Book responseBook = bookService.updateBook(BigInteger.valueOf(123), book);

// Assert the expected values
        assertEquals(BigInteger.valueOf(123), responseBook.getId());
        assertEquals("Book Name", responseBook.getName());
        assertEquals("Author", responseBook.getAuthor());
        assertEquals("Category", responseBook.getCategory());
        assertEquals("ISBN", responseBook.getIsbn());
        assertFalse(responseBook.getIsDeleted());
    }

    @Test
    void testDeleteBook_BookCanBeDeleted() throws Exception {
// Mock the result of deleteBook function call
        RemoteFunctionCall<TransactionReceipt> deleteBookFunction = mock(RemoteFunctionCall.class);
        TransactionReceipt transactionReceipt = mock(TransactionReceipt.class);
        when(deleteBookFunction.send()).thenReturn(transactionReceipt);
        when(libraryContract.deleteBook(BigInteger.valueOf(1))).thenReturn(deleteBookFunction);

// Call the deleteBook method
        assertDoesNotThrow(() -> bookService.deleteBook(BigInteger.valueOf(1)));

// Verify that deleteBookFunction.send() was called
        verify(deleteBookFunction, times(1)).send();
    }

}