package com.dev.library.service.borrowing_management.implementation;

import com.dev.library.config.ApplicationProperties;
import com.dev.library.exception.BookAlreadyIssuedException;
import com.dev.library.exception.BorrowerNotMatch;
import com.dev.library.dto.borrowing_management.BookBorrowRequest;
import com.dev.library.dto.borrowing_management.BookBorrowResponse;
import com.dev.library.dto.borrowing_management.UpdateBookBorrowRequest;
import com.dev.library.service.borrowing_management.BorrowedBooksService;
import com.dev.library.utility.CryptoUtils;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BorrowedBooksServiceImpl implements BorrowedBooksService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private LibraryContract_updated libraryContract;

    private final ApplicationProperties applicationProperties;
    public static final String SERVICE = "Borrowed Book Service Impl";

    public BorrowedBooksServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }


    public void setLibraryContract(LibraryContract_updated libraryContract) {
        this.libraryContract = libraryContract;
    }

    @PostConstruct
    private void initialize() {
        try {
            Web3j web3 = Web3j.build(new HttpService(applicationProperties.getRpcURL()));
            Credentials credentials = Credentials.create(applicationProperties.getWalletCredential());
            libraryContract = LibraryContract_updated.load(
                    "0x"+applicationProperties.getContractAddress(),
                    web3,
                    credentials,
                    new DefaultGasProvider()
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred during initialization at BorrowedBook Service", e);
        }
    }


    // Fetch all the borrowed book records
    public List<BookBorrowResponse> getAllBooksBorrowed() throws Exception {
        RemoteFunctionCall<List> getAllBorrowedBooksFunction = libraryContract.getAllBorrowedBooks();
        List result = getAllBorrowedBooksFunction.send();

        List<BookBorrowResponse> borrowedBooks = new ArrayList<>();

        for (Object item : result) {
            if (item instanceof LibraryContract_updated.BorrowedBook) {
                LibraryContract_updated.BorrowedBook borrowedBookData = (LibraryContract_updated.BorrowedBook) item;
                LibraryContract_updated.Book bookData = libraryContract.getBook(borrowedBookData.bookId).send();
                if (Boolean.FALSE.equals(bookData.isDeleted)) {
                    BigInteger id = borrowedBookData.id;
                    String bookName = bookData.name;
                    String borrowerAddress = borrowedBookData.borrowerAddress;
                    LocalDate issueDate = Instant.ofEpochMilli(1000L * borrowedBookData.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate returnDate = null;
                    if (!(borrowedBookData.returnDate.intValue() == 0)) {
                        returnDate = Instant.ofEpochMilli(1000L * borrowedBookData.returnDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate();
                    }
                    boolean isLost = borrowedBookData.isLost;
                    String borrowerPhoneNumber = CryptoUtils.decrypt(borrowedBookData.borrowerPhoneNumber);
                    String borrowerName = CryptoUtils.decrypt(borrowedBookData.borrowerName);
                    BookBorrowResponse borrowedBook = new BookBorrowResponse();
                    borrowedBook.setId(id);
                    borrowedBook.setBookName(bookName);
                    borrowedBook.setBorrowerAddress(borrowerAddress);
                    borrowedBook.setIssueDate(issueDate);
                    borrowedBook.setReturnDate(returnDate);
                    borrowedBook.setLost(isLost);
                    borrowedBook.setBorrowerName(borrowerName);
                    borrowedBook.setBorrowerPhone(borrowerPhoneNumber);
                    borrowedBooks.add(borrowedBook);
                }
            }
        }
        logger.log(Level.INFO,"{0} - All borrowed books record retrieved", SERVICE);
        return borrowedBooks;
    }


    //Add a borrowed book details
    public BookBorrowResponse addBookBorrow(BigInteger bookId, BookBorrowRequest bookBorrowRequest) {
        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
        try {
            RemoteFunctionCall<TransactionReceipt> addBorrowBook = libraryContract.borrowBook(
                    bookId,
                    CryptoUtils.encrypt(bookBorrowRequest.getBorrowerName()),
                    CryptoUtils.encrypt(bookBorrowRequest.getBorrowerPhone()),
                    bookBorrowRequest.getWalletAddress(),
                    BigInteger.valueOf(bookBorrowRequest.getIssueDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond())
            );
            TransactionReceipt transactionReceipt = addBorrowBook.send();
            List<LibraryContract_updated.BookBorrowedEventResponse> bookBorrowedEvents = LibraryContract_updated.getBookBorrowedEvents(transactionReceipt);
            if (!bookBorrowedEvents.isEmpty()) {
                LibraryContract_updated.BookBorrowedEventResponse bookBorrowedEvent = bookBorrowedEvents.get(0);
                borrowedBookResponse.setId(bookBorrowedEvent.borrowedBookId);
                borrowedBookResponse.setBookName(bookBorrowedEvent.bookName);
                borrowedBookResponse.setBorrowerName(CryptoUtils.decrypt(bookBorrowedEvent.borrowerName));
                borrowedBookResponse.setBorrowerAddress(bookBorrowedEvent.borrower);
                borrowedBookResponse.setBorrowerPhone(CryptoUtils.decrypt(bookBorrowedEvent.borrowerPhoneNumber));
                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookBorrowedEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setLost(false);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("You have already borrowed this book")) {
                throw new BookAlreadyIssuedException("This book has already been issued to the borrower");
            }
        }
        logger.log(Level.INFO,"{0} - Book borrowed successfully, borrowed Id: {1}", new Object[]{SERVICE, borrowedBookResponse.getId()});
        return borrowedBookResponse;
    }

     // To update the book return date and update the borrow book as returned.
    public BookBorrowResponse updateBorrowed(BigInteger bookId, BigInteger bookBorrowId, UpdateBookBorrowRequest updateBookBorrowRequest) {
        if (Boolean.TRUE.equals(updateBookBorrowRequest.isLost())) {
            return bookLost(bookBorrowId, updateBookBorrowRequest.getBorrowerAddress());
        }
        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
        RemoteFunctionCall<TransactionReceipt> updateBorrowBook = libraryContract.returnBook(bookBorrowId, BigInteger.valueOf(updateBookBorrowRequest.getReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()), updateBookBorrowRequest.getBorrowerAddress());
        try {
            TransactionReceipt transactionReceipt = updateBorrowBook.send();
            List<LibraryContract_updated.BookReturnedEventResponse> bookReturnEvents = LibraryContract_updated.getBookReturnedEvents(transactionReceipt);
            if (!bookReturnEvents.isEmpty()) {
                LibraryContract_updated.BookReturnedEventResponse bookReturnEvent = bookReturnEvents.get(0);
                borrowedBookResponse.setId(bookReturnEvent.borrowedBookId);
                borrowedBookResponse.setBorrowerName(CryptoUtils.decrypt(bookReturnEvent.borrowerName));
                borrowedBookResponse.setBookName(bookReturnEvent.bookName);
                borrowedBookResponse.setBorrowerAddress(bookReturnEvent.borrowerAddress);
                borrowedBookResponse.setBorrowerPhone(CryptoUtils.decrypt(bookReturnEvent.borrowerPhoneNumber));
                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookReturnEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setReturnDate(Instant.ofEpochMilli(1000L * bookReturnEvent.returnDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setLost(false);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Book not borrowed from this address"))
                throw new BorrowerNotMatch("Should be returned by the same borrower");
        }
        logger.log(Level.INFO,"{0} - Book returned successfully on : {1}", new Object[]{SERVICE,  borrowedBookResponse.getReturnDate()});
        return borrowedBookResponse;
    }


    public BookBorrowResponse bookLost(BigInteger bookBorrowId, String borrowerAddress) {
        BookBorrowResponse borrowedBookResponse = new BookBorrowResponse();
        RemoteFunctionCall<TransactionReceipt> markBookLost = libraryContract.reportLost(bookBorrowId, borrowerAddress);
        try {
            TransactionReceipt transactionReceipt = markBookLost.send();
            List<LibraryContract_updated.BookLostEventResponse> bookLostEvents = LibraryContract_updated.getBookLostEvents(transactionReceipt);
            if (!bookLostEvents.isEmpty()) {
                LibraryContract_updated.BookLostEventResponse bookLostEvent = bookLostEvents.get(0);
                borrowedBookResponse.setId(bookLostEvent.borrowedBookId);
                borrowedBookResponse.setBorrowerName(CryptoUtils.decrypt(bookLostEvent.borrowerName));
                borrowedBookResponse.setBookName(bookLostEvent.bookName);
                borrowedBookResponse.setBorrowerAddress(bookLostEvent.borrowerAddress);
                borrowedBookResponse.setBorrowerPhone(CryptoUtils.decrypt(bookLostEvent.borrowerPhoneNumber));
                borrowedBookResponse.setIssueDate(Instant.ofEpochMilli(1000L * bookLostEvent.issueDate.intValue()).atZone(ZoneId.systemDefault()).toLocalDate());
                borrowedBookResponse.setLost(true);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Book not borrowed from this address"))
                throw new BorrowerNotMatch("Should be reported by the borrower");
        }
        logger.log(Level.INFO,"{0} - Borrowed book marked as lost",SERVICE);
        return borrowedBookResponse;
    }

}