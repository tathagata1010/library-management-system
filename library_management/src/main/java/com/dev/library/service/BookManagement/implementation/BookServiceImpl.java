package com.dev.library.service.BookManagement.implementation;

import com.dev.library.entity.Book;
import com.dev.library.exception.BookAlreadyExistsException;
import com.dev.library.exception.BookCannotBeDeletedException;
import com.dev.library.exception.BookNotFoundException;
import com.dev.library.exception.NotLibrarianException;
import com.dev.library.utility.Constants;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookServiceImpl {

    private LibraryContract_updated libraryContract;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void setLibraryContract(LibraryContract_updated libraryContract) {
        this.libraryContract = libraryContract;
    }

    @PostConstruct
    public void initialize() {
        try {
            Web3j web3 = Web3j.build(new HttpService(Constants.RPC_URL));
            Credentials credentials = Credentials.create(Constants.CREDENTIAL);

            libraryContract = LibraryContract_updated.load(
                    Constants.CONTRACT_ADDRESS,
                    web3,
                    credentials,
                    new DefaultGasProvider()
            );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Book addBook(Book book) throws Exception {
        Book responseBook = new Book();
        try {
            RemoteFunctionCall<TransactionReceipt> addBookFunction = libraryContract.addBook(
                    book.getName(),
                    book.getAuthor(),
                    book.getCategory(),
                    false
            );
            TransactionReceipt transactionReceipt = addBookFunction.send();
            List<LibraryContract_updated.BookAddedEventResponse> bookAddedEvents = LibraryContract_updated.getBookAddedEvents(transactionReceipt);
            if (!bookAddedEvents.isEmpty()) {
                LibraryContract_updated.BookAddedEventResponse bookAddedEvent = bookAddedEvents.get(0);
                responseBook.setId(bookAddedEvent.id);
                responseBook.setName(bookAddedEvent.name);
                responseBook.setAuthor(bookAddedEvent.author);
                responseBook.setCategory(bookAddedEvent.category);
                responseBook.setIsDeleted(bookAddedEvent.isDeleted);
            }
        } catch (TransactionException e) {
            if (e.getMessage().contains("Another book with the same name already exists"))
                throw new BookAlreadyExistsException("Book with name " + book.getName() + " already exists!");
        }
        logger.info("Book added successfully book Id: " + responseBook.getId());
        return responseBook;
    }


    public List<Book> getAllBooks() throws Exception {
        RemoteFunctionCall<List> getAllBooksFunction = libraryContract.getAllBooks();
        List result = getAllBooksFunction.send();

        List<Book> books = new ArrayList<>();
        for (Object item : result) {
            if (item instanceof LibraryContract_updated.Book && Boolean.TRUE.equals(!((LibraryContract_updated.Book) item).isDeleted)) {
                BigInteger value = ((LibraryContract_updated.Book) item).id;
                String name = ((LibraryContract_updated.Book) item).name;
                String author = ((LibraryContract_updated.Book) item).author;
                String category = ((LibraryContract_updated.Book) item).category;
                boolean isDeleted = ((LibraryContract_updated.Book) item).isDeleted;
                Book book = new Book(value, name, author, category, isDeleted);
                books.add(book);
            }
        }
        return books;
    }

    public Book getBookByName(String name) throws BookNotFoundException {
        try {
            RemoteFunctionCall<LibraryContract_updated.Book> getBookCall = libraryContract.getBookByName(name);
            LibraryContract_updated.Book bookData = getBookCall.send();
            BigInteger value = bookData.id;
            String author = bookData.author;
            String category = bookData.category;
            boolean isDeleted = bookData.isDeleted;
            return new Book(value, name, author, category, isDeleted);
        } catch (Exception e) {
            throw new BookNotFoundException(Constants.BOOK_NOT_FOUND_NAME + name);
        }
    }

    public Book updateBook(BigInteger id, Book book) {

        Book responseBook = new Book();
        try {
            RemoteFunctionCall<TransactionReceipt> updateBookFunction = libraryContract.updateBook(id, book.getName(), book.getAuthor(), book.getCategory());
            TransactionReceipt transactionReceipt = updateBookFunction.send();
            List<LibraryContract_updated.BookUpdatedEventResponse> bookUpdatedEvent = LibraryContract_updated.getBookUpdatedEvents(transactionReceipt);

            if (!bookUpdatedEvent.isEmpty()) {
                LibraryContract_updated.BookUpdatedEventResponse bookUpdatedEventResponse = bookUpdatedEvent.get(0);

                responseBook.setId(bookUpdatedEventResponse.id);
                responseBook.setName(bookUpdatedEventResponse.name);
                responseBook.setAuthor(bookUpdatedEventResponse.author);
                responseBook.setCategory(bookUpdatedEventResponse.category);
                responseBook.setIsDeleted(bookUpdatedEventResponse.isDeleted);

            }

        } catch (Exception e) {
            if (e.getMessage().contains("Another book with the same name already exists")) {
                throw new BookAlreadyExistsException("Book with name " + book.getName() + " already exists!");
            } else if (e.getMessage().contains("Only the contract owner can call this function")) {
                throw new NotLibrarianException(Constants.ONLY_LIBRARIAN_ACCESS);
            }
        }
        logger.info("Book details updated successfully for book Id: " + responseBook.getId());
        return responseBook;
    }

    public void deleteBook(BigInteger id) {
        try {
            System.out.println(id);
            RemoteFunctionCall<TransactionReceipt> deleteBookFunction = libraryContract.deleteBook(id);
            deleteBookFunction.send();
        } catch (Exception e) {
            if (e.getMessage().contains("Book cannot be deleted")) {
                throw new BookCannotBeDeletedException(Constants.BOOK_CURRENTLY_BORROWED);
            } else if (e.getMessage().contains("Only the contract owner can call this function")) {
                throw new NotLibrarianException(Constants.ONLY_LIBRARIAN_ACCESS);
            }
        }
        logger.info("Book deleted successfully");
    }
}