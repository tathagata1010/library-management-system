package com.dev.library.service.BookManagement.implementation;

import com.dev.library.entity.Book;
import com.dev.library.exception.BookAlreadyExistsException;
import com.dev.library.exception.BookCannotBeDeletedException;
import com.dev.library.exception.BookNotFoundException;
import com.dev.library.utility.Constants;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.librarycontract.LibraryContract;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl {

//    private LibraryContract libraryContract;

    private LibraryContract_updated libraryContract;

//    public void setLibraryContract(LibraryContract libraryContract) {
//        this.libraryContract = libraryContract;
//    }

    public void setLibraryContract(LibraryContract_updated libraryContract) {
        this.libraryContract = libraryContract;
    }

    @PostConstruct
    public void initialize() {
        try {
            Web3j web3 = Web3j.build(new HttpService(Constants.RPC_URL));
            Credentials credentials = Credentials.create(Constants.CREDENTIAL);

            EthBlock block = web3.ethGetBlockByNumber(
                    DefaultBlockParameterName.LATEST,
                    false
            ).send();

            ContractGasProvider contractGasProvider = new StaticGasProvider(
                    web3.ethGasPrice().send().getGasPrice(),
                    block.getBlock().getGasLimit()
            );

//            libraryContract = LibraryContract.load(
//                    Constants.CONTRACT_ADDRESS,
//                    web3,
//                    credentials,
//                    contractGasProvider
//            );

            libraryContract = LibraryContract_updated.load(
                    Constants.CONTRACT_ADDRESS,
                    web3,
                    credentials,
                    contractGasProvider
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public Book addBook(Book book) throws Exception {
//        List<Book> books = getAllBooks();
//
//        for (Book bookCheck : books) {
//             if (bookCheck.getName().equals(book.getName()) && !bookCheck.getIsDeleted()) {
//                throw new BookAlreadyExistsException("Book with name " + book.getName() + " already exists!");
//            }
//        }
//
//        RemoteFunctionCall<TransactionReceipt> addBookFunction = libraryContract.addBook(
//                book.getName(),
//                book.getAuthor(),
//                book.getCategory(),
//                false
//        );
//
//        TransactionReceipt transactionReceipt = addBookFunction.send();
//        System.out.println(transactionReceipt);
//        Book responseBook = new Book();
//        List bookAddedEvents = LibraryContract.getBookAddedEvents(transactionReceipt);
//        if (!bookAddedEvents.isEmpty()) {
//            LibraryContract.BookAddedEventResponse bookAddedEvent = (LibraryContract.BookAddedEventResponse) bookAddedEvents.get(0);
//
//            responseBook.setId(bookAddedEvent.id);
//            responseBook.setName(bookAddedEvent.name);
//            responseBook.setAuthor(bookAddedEvent.author);
//            responseBook.setCategory(bookAddedEvent.category);
//            responseBook.setIsDeleted(bookAddedEvent.isDeleted);
//            System.out.println(responseBook);
//        }
//        return responseBook;
//    }


    public Book addBook(Book book) throws Exception {
        List<Book> books = getAllBooks();

        for (Book bookCheck : books) {
            if (bookCheck.getName().equals(book.getName()) && !bookCheck.getIsDeleted()) {
                throw new BookAlreadyExistsException("Book with name " + book.getName() + " already exists!");
            }
        }
        RemoteFunctionCall<TransactionReceipt> addBookFunction = libraryContract.addBook(
                book.getName(),
                book.getAuthor(),
                book.getCategory(),
                false
        );
        TransactionReceipt transactionReceipt = addBookFunction.send();

        System.out.println(transactionReceipt);

        Book responseBook = new Book();
        List bookAddedEvents = libraryContract.getBookAddedEvents(transactionReceipt);
        if (!bookAddedEvents.isEmpty()) {
            LibraryContract_updated.BookAddedEventResponse bookAddedEvent = (LibraryContract_updated.BookAddedEventResponse) bookAddedEvents.get(0);
            responseBook.setId(bookAddedEvent.id);
            responseBook.setName(bookAddedEvent.name);
            responseBook.setAuthor(bookAddedEvent.author);
            responseBook.setCategory(bookAddedEvent.category);
            responseBook.setIsDeleted(bookAddedEvent.isDeleted);
            System.out.println(responseBook);
        }

        return responseBook;
    }


//    public List<Book> getAllBooks() throws Exception {
//        RemoteFunctionCall<List> getAllBooksFunction = libraryContract.getAllBooks();
//        List result = getAllBooksFunction.send();
//        System.out.println(result);
//
//        List<Book> books = new ArrayList<>();
//        for (Object item : result) {
//            if (item instanceof LibraryContract.Book && Boolean.TRUE.equals(!((LibraryContract.Book) item).isDeleted)) {
//                BigInteger value = ((LibraryContract.Book) item).id;
//                String name = ((LibraryContract.Book) item).name;
//                String author = ((LibraryContract.Book) item).author;
//                String category = ((LibraryContract.Book) item).category;
//                boolean isDeleted = ((LibraryContract.Book) item).isDeleted;
//                Book book = new Book(value, name, author, category,isDeleted);
//                books.add(book);
//            }
//        }
//
//        return books;
//    }


        public List<Book> getAllBooks() throws Exception {
        RemoteFunctionCall<List> getAllBooksFunction = libraryContract.getAllBooks();
        List result = getAllBooksFunction.send();
        System.out.println(result);

        List<Book> books = new ArrayList<>();
        for (Object item : result) {
            if (item instanceof LibraryContract_updated.Book && Boolean.TRUE.equals(!((LibraryContract_updated.Book) item).isDeleted)) {
                BigInteger value = ((LibraryContract_updated.Book) item).id;
                String name = ((LibraryContract_updated.Book) item).name;
                String author = ((LibraryContract_updated.Book) item).author;
                String category = ((LibraryContract_updated.Book) item).category;
                boolean isDeleted = ((LibraryContract_updated.Book) item).isDeleted;
                Book book = new Book(value, name, author, category,isDeleted);
                books.add(book);
            }
        }

        return books;
    }

    public Book getBookByName(String name) throws BookNotFoundException {
        try {
            RemoteFunctionCall<LibraryContract_updated.Book> getBookCall = libraryContract.getBookByName(name);
            LibraryContract_updated.Book bookData = getBookCall.send();
            System.out.println(bookData);
            BigInteger value = bookData.id;
            String author = bookData.author;
            String category = bookData.category;
            boolean isDeleted = bookData.isDeleted;
            return new Book(value, name, author, category,isDeleted);
        } catch (Exception e) {
            throw new BookNotFoundException(Constants.BOOK_NOT_FOUND_NAME + name);
        }
    }

    public Book updateBook(BigInteger id, Book book) throws Exception {

        Book responseBook=new Book();
        try {
            RemoteFunctionCall<TransactionReceipt> updateBookFunction = libraryContract.updateBook(id, book.getName(), book.getAuthor(), book.getCategory());
            TransactionReceipt transactionReceipt=updateBookFunction.send();
            List bookUpdatedEvent= LibraryContract_updated.getBookUpdatedEvents(transactionReceipt);

            if (!bookUpdatedEvent.isEmpty()) {
                LibraryContract_updated.BookUpdatedEventResponse bookUpdatedEventResponse = (LibraryContract_updated.BookUpdatedEventResponse) bookUpdatedEvent.get(0);

                responseBook.setId(bookUpdatedEventResponse.id);
                responseBook.setName(bookUpdatedEventResponse.name);
                responseBook.setAuthor(bookUpdatedEventResponse.author);
                responseBook.setCategory(bookUpdatedEventResponse.category);
                responseBook.setIsDeleted(bookUpdatedEventResponse.isDeleted);
                System.out.println(responseBook);
            }

        } catch (Exception e) {
            if (e.getMessage().contains("Another book with the same name already exists")) {
                throw new BookAlreadyExistsException("Book with name " + book.getName() + " already exists!");
            }
        }
        return responseBook;
    }

    public void deleteBook(BigInteger id) {
        try {
            RemoteFunctionCall<TransactionReceipt> deleteBookFunction = libraryContract.deleteBook(id);
            deleteBookFunction.send();
        } catch (Exception e) {
            if (e.getMessage().contains("Book is currently borrowed")) {
                throw new BookCannotBeDeletedException(Constants.BOOK_CURRENTLY_BORROWED);
            }
        }
    }
}