package com.dev.library.service.BorrowingManagement.implementation;

import com.dev.library.exception.BookAlreadyIssuedException;
import com.dev.library.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library.model.BorrowingManagement.UpdateBookBorrowRequest;
import com.dev.library.service.BookManagement.implementation.BookServiceImpl;
import com.dev.library.service.BorrowingManagement.BorrowedBooksService;
import com.dev.library.utility.Constants;
import org.springframework.stereotype.Service;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowedBooksServiceImpl implements BorrowedBooksService {
    private LibraryContract_updated libraryContract;


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

//    public List<BookBorrowResponse> getAllBooksBorrowed() {
//        RemoteFunctionCall<List> getAllBooksBorrowedFunction = libraryContract.getBorrowedBooks();
//        List result = null;
//        try {
//            result = getAllBooksBorrowedFunction.send();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        List<BookBorrowResponse> borrowedBooks = new ArrayList<>();
//        for (Object item : result) {
//            if (item instanceof LibraryContract.BorrowedBookResponse && ((LibraryContract.BorrowedBookResponse) item).id.intValue() != 0) {
//                BigInteger id = ((LibraryContract.BorrowedBookResponse) item).id;
//                String bookName = ((LibraryContract.BorrowedBookResponse) item).bookName;
//                String borrowerName = ((LibraryContract.BorrowedBookResponse) item).borrowerName;
//                String borrowerPhoneNumber = ((LibraryContract.BorrowedBookResponse) item).borrowerPhoneNumber;
//                LocalDate issueDate = Instant.ofEpochMilli(1000L * ((LibraryContract.BorrowedBookResponse) item).issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate();
//                LocalDate returnDate = null;
//                if (((LibraryContract.BorrowedBookResponse) item).returnDate.intValue() != 0)
//                    returnDate = Instant.ofEpochMilli(1000L * ((LibraryContract.BorrowedBookResponse) item).returnDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate();
//                boolean isLost = ((LibraryContract.BorrowedBookResponse) item).isLost;
//                BookBorrowResponse borrowedBook = new BookBorrowResponse(id, issueDate, borrowerName, borrowerPhoneNumber, bookName, returnDate, isLost);
//                borrowedBooks.add(borrowedBook);
//            }
//        }
//        return borrowedBooks;
//    }

    public List<BookBorrowResponse> getAllBooksBorrowed() throws Exception {
        RemoteFunctionCall<List> getAllBorrowedBooksFunction = libraryContract.getAllBorrowedBooks();
        List result = getAllBorrowedBooksFunction.send();

        List<BookBorrowResponse> borrowedBooks = new ArrayList<>();

        for (Object item : result) {
            if (item instanceof LibraryContract_updated.BorrowedBook) {
                LibraryContract_updated.BorrowedBook borrowedBookData = (LibraryContract_updated.BorrowedBook) item;
                LibraryContract_updated.Book bookData = libraryContract.getBook(borrowedBookData.id).send();
                if (Boolean.FALSE.equals(bookData.isDeleted)) {
                    BigInteger id = borrowedBookData.id;
                    String bookName = bookData.name;
                    String borrowerAddress = borrowedBookData.borrowerAddress;
                    LocalDate issueDate = Instant.ofEpochMilli(1000L * borrowedBookData.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate returnDate = null;
                if (borrowedBookData.returnDate.intValue() != 0) {
                    returnDate = Instant.ofEpochMilli(1000L * borrowedBookData.returnDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate();
                }

                boolean isLost = borrowedBookData.isLost;
                    String borrowerPhoneNumber = borrowedBookData.borrowerPhoneNumber;
                    BookBorrowResponse borrowedBook = new BookBorrowResponse(id, issueDate, borrowerAddress, borrowerPhoneNumber, bookName, returnDate, isLost);
                    borrowedBooks.add(borrowedBook);
                }
            }
        }

        return borrowedBooks;
    }


//    public BookBorrowResponse addBookBorrow(BigInteger bookId, BookBorrowRequest bookBorrowRequest) {
//        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
//        try {
//            System.out.println(BigInteger.valueOf(bookBorrowRequest.getIssueDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()));
//            RemoteFunctionCall<TransactionReceipt> addBorrowBook = libraryContract.borrowBook(
//                    bookId,
//                    bookBorrowRequest.getBorrowerName(),
//                    bookBorrowRequest.getBorrowerPhone(),
//                    bookBorrowRequest.getWalletAddress(),
//                    BigInteger.valueOf(bookBorrowRequest.getIssueDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond())
//            );
//            TransactionReceipt transactionReceipt = addBorrowBook.send();
//            System.out.println(transactionReceipt);
//            List bookBorrowedEvents = LibraryContract.getBookBorrowedEvents(transactionReceipt);
//            if (!bookBorrowedEvents.isEmpty()) {
//                LibraryContract.BookBorrowedEventResponse bookBorrowedEvent = (LibraryContract.BookBorrowedEventResponse) bookBorrowedEvents.get(0);
//                borrowedBookResponse.setId(bookBorrowedEvent.borrowedBookId);
//                borrowedBookResponse.setBookName(bookBorrowedEvent.bookName);
//                borrowedBookResponse.setBorrowerName(bookBorrowedEvent.borrowerName);
//                borrowedBookResponse.setBorrowerPhone(bookBorrowedEvent.borrowerPhoneNumber);
//                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookBorrowedEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
//                borrowedBookResponse.setLost(false);
//                System.out.println(borrowedBookResponse);
//            }
//        } catch (Exception e) {
//            if (e.getMessage().contains("BOOK_ALREADY_BORROWED")) {
//                throw new BookAlreadyIssuedException("This book has already been issued to the borrower");
//            }
//        }
//        return borrowedBookResponse;
//    }


    public BookBorrowResponse addBookBorrow(BigInteger bookId, BookBorrowRequest bookBorrowRequest) {
        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
        try {
            RemoteFunctionCall<TransactionReceipt> addBorrowBook = libraryContract.borrowBook(
                    bookId,
                    bookBorrowRequest.getBorrowerPhone(),
                    bookBorrowRequest.getWalletAddress(),
                    BigInteger.valueOf(bookBorrowRequest.getIssueDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond())
            );
            TransactionReceipt transactionReceipt = addBorrowBook.send();
            List bookBorrowedEvents = LibraryContract_updated.getBookBorrowedEvents(transactionReceipt);
            if (!bookBorrowedEvents.isEmpty()) {
                LibraryContract_updated.BookBorrowedEventResponse bookBorrowedEvent = (LibraryContract_updated.BookBorrowedEventResponse) bookBorrowedEvents.get(0);
                System.out.println(bookBorrowedEvent);
                borrowedBookResponse.setId(bookBorrowedEvent.borrowedBookId);
                borrowedBookResponse.setBookName(bookBorrowedEvent.bookName);
                borrowedBookResponse.setBorrowerAddress(bookBorrowedEvent.borrower);
                borrowedBookResponse.setBorrowerPhone(bookBorrowedEvent.borrowerPhoneNumber);
                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookBorrowedEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setLost(false);
              }
        } catch (Exception e) {
            if (e.getMessage().contains("You have already borrowed this book")) {
                throw new BookAlreadyIssuedException("This book has already been issued to the borrower");
            }
        }
        System.out.println(borrowedBookResponse.toString());
        return borrowedBookResponse;
    }



//    public BookBorrowResponse updateBorrowed(BigInteger bookId, BigInteger bookBorrowId, UpdateBookBorrowRequest updateBookBorrowRequest) {
//        if (Boolean.TRUE.equals(updateBookBorrowRequest.getLost())) {
//            return bookLost(bookBorrowId);
//        }
//        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
//        RemoteFunctionCall<TransactionReceipt> updateBorrowBook = libraryContract.returnBook(bookBorrowId, BigInteger.valueOf(updateBookBorrowRequest.getReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()));
//        try {
//            TransactionReceipt transactionReceipt = updateBorrowBook.send();
//            List bookReturnEvents = LibraryContract.getBookReturnedEvents(transactionReceipt);
//            if (!bookReturnEvents.isEmpty()) {
//                LibraryContract.BookReturnedEventResponse bookReturnEvent = (LibraryContract.BookReturnedEventResponse) bookReturnEvents.get(0);
//                borrowedBookResponse.setId(bookReturnEvent.borrowedBookId);
//                borrowedBookResponse.setBookName(bookReturnEvent.bookName);
//                borrowedBookResponse.setBorrowerName(bookReturnEvent.borrowerName);
//                borrowedBookResponse.setBorrowerPhone(bookReturnEvent.borrowerPhoneNumber);
//                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookReturnEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
//                borrowedBookResponse.setReturnDate(Instant.ofEpochMilli(1000L * bookReturnEvent.returnDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
//                borrowedBookResponse.setLost(false);
//                System.out.println(borrowedBookResponse);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return borrowedBookResponse;
//    }


    public BookBorrowResponse updateBorrowed(BigInteger bookId, BigInteger bookBorrowId, UpdateBookBorrowRequest updateBookBorrowRequest) {
        if (Boolean.TRUE.equals(updateBookBorrowRequest.getLost())) {
            return bookLost(bookBorrowId);
        }
        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
        RemoteFunctionCall<TransactionReceipt> updateBorrowBook = libraryContract.returnBook(bookBorrowId, BigInteger.valueOf(updateBookBorrowRequest.getReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()));
        try {
            TransactionReceipt transactionReceipt = updateBorrowBook.send();
            List bookReturnEvents = LibraryContract_updated.getBookReturnedEvents(transactionReceipt);
            if (!bookReturnEvents.isEmpty()) {
                LibraryContract_updated.BookReturnedEventResponse bookReturnEvent = (LibraryContract_updated.BookReturnedEventResponse) bookReturnEvents.get(0);
                borrowedBookResponse.setId(bookReturnEvent.borrowedBookId);
                borrowedBookResponse.setBookName(bookReturnEvent.bookName);
                borrowedBookResponse.setBorrowerAddress(bookReturnEvent.borrowerAddress);
                borrowedBookResponse.setBorrowerPhone(bookReturnEvent.borrowerPhoneNumber);
                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookReturnEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setReturnDate(Instant.ofEpochMilli(1000L * bookReturnEvent.returnDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setLost(false);
                System.out.println(borrowedBookResponse);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return borrowedBookResponse;
    }

//    public BookBorrowResponse bookLost(BigInteger bookBorrowId) {
//        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
//        RemoteFunctionCall<TransactionReceipt> addBorrowBook = libraryContract.markBookLost(bookBorrowId);
//        try {
//            TransactionReceipt transactionReceipt = addBorrowBook.send();
//            List bookLostEvents = LibraryContract.getBookLostEvents(transactionReceipt);
//            if (!bookLostEvents.isEmpty()) {
//                LibraryContract.BookLostEventResponse bookLostEvent = (LibraryContract.BookLostEventResponse) bookLostEvents.get(0);
//                borrowedBookResponse.setId(bookLostEvent.borrowedBookId);
//                borrowedBookResponse.setBookName(bookLostEvent.bookName);
//                borrowedBookResponse.setBorrowerName(bookLostEvent.borrowerName);
//                borrowedBookResponse.setBorrowerPhone(bookLostEvent.borrowerPhoneNumber);
//                borrowedBookResponse.setLost(true);
//                System.out.println(borrowedBookResponse);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return borrowedBookResponse;
//    }

    public BookBorrowResponse bookLost(BigInteger bookBorrowId) {
        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
        RemoteFunctionCall<TransactionReceipt> addBorrowBook = libraryContract.reportLost(bookBorrowId);
        try {
            TransactionReceipt transactionReceipt = addBorrowBook.send();
            List bookLostEvents = LibraryContract.getBookLostEvents(transactionReceipt);
            if (!bookLostEvents.isEmpty()) {
                LibraryContract_updated.BookLostEventResponse bookLostEvent = (LibraryContract_updated.BookLostEventResponse) bookLostEvents.get(0);
                borrowedBookResponse.setId(bookLostEvent.borrowedBookId);
                borrowedBookResponse.setBookName(bookLostEvent.bookName);
                borrowedBookResponse.setBorrowerAddress(bookLostEvent.borrowerAddress);
                borrowedBookResponse.setBorrowerPhone(bookLostEvent.borrowerPhoneNumber);
                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookLostEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setLost(true);
                System.out.println(borrowedBookResponse);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return borrowedBookResponse;
    }
}