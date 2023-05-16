package com.dev.library_management.service.BorrowingManagement.implementation;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.dao.BorrowedBooksDao;
import com.dev.library_management.dao.BorrowerDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.exception.BookAlreadyIssuedException;
import com.dev.library_management.exception.BorrowedNotFoundException;
import com.dev.library_management.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library_management.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library_management.model.BorrowingManagement.UpdateBookBorrowRequest;
import com.dev.library_management.service.BookManagement.implementation.BookServiceImpl;
import com.dev.library_management.service.BorrowingManagement.BorrowedBooksService;
import com.dev.library_management.utility.Constants;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BorrowedBooksServiceImpl implements BorrowedBooksService {

    private final BorrowedBooksDao borrowedBooksDao;
    private final BookDao bookDao;
    private final BorrowerDao borrowerDao;
    private BookServiceImpl bookServiceImpl;
    public BorrowedBooksServiceImpl(BorrowedBooksDao borrowedBooksDao, BookServiceImpl bookServiceImpl, BookDao bookDao, BorrowerDao borrowerDao) {
        this.borrowedBooksDao = borrowedBooksDao;
        this.bookDao = bookDao;
        this.bookServiceImpl = bookServiceImpl;
        this.borrowerDao = borrowerDao;
    }

    public List<BookBorrowResponse> getAllBookReports() {
        List<BorrowedBooks> borrowedBooks = borrowedBooksDao.findAllByOrderByIssueDateAsc();
        List<BookBorrowResponse> bookDetailsList = new ArrayList<>();
        for (BorrowedBooks borrowedBook : borrowedBooks) {
            if(borrowedBook.getBook().getIsDeleted()!=1) {
                BookBorrowResponse bookBorrowResponse = new BookBorrowResponse();
                bookBorrowResponse.setId(borrowedBook.getId());
                bookBorrowResponse.setIssueDate(borrowedBook.getIssueDate());
                bookBorrowResponse.setBorrowerName(borrowedBook.getBorrower().getName());
                bookBorrowResponse.setBorrowerPhone(borrowedBook.getBorrower().getPhoneNumber());
                bookBorrowResponse.setBookName(borrowedBook.getBook().getName());
                bookBorrowResponse.setReturnDate(borrowedBook.getReturnDate());
                bookBorrowResponse.setLost(borrowedBook.getLost());
                bookDetailsList.add(bookBorrowResponse);
            }
        }
        return bookDetailsList;
    }

    public BookBorrowResponse getBookReportById(Long id) throws BorrowedNotFoundException {
        try {
            Optional<BorrowedBooks> bookReport = borrowedBooksDao.findById(id);
            if (bookReport.isEmpty()) {
                throw new BorrowedNotFoundException(Constants.BORROWED_NOT_FOUND + id);
            }
            BookBorrowResponse bookBorrowResponse =new BookBorrowResponse();
            bookBorrowResponse.setId(bookReport.get().getId());
            bookBorrowResponse.setBookName(bookReport.get().getBook().getName());
            bookBorrowResponse.setBorrowerName(bookReport.get().getBorrower().getName());
            bookBorrowResponse.setBorrowerPhone(bookReport.get().getBorrower().getPhoneNumber());
            bookBorrowResponse.setReturnDate(bookReport.get().getReturnDate());
            bookBorrowResponse.setIssueDate(bookReport.get().getIssueDate());
            return bookBorrowResponse;
        } catch (Exception e) {
            throw new BorrowedNotFoundException(Constants.BORROWED_NOT_FOUND + id);
        }
    }

    public List<BookBorrowResponse> getBorrowedBooksByBookId(Long bookId) {
        List<BorrowedBooks> borrowedBooksList = borrowedBooksDao.findByBookId(bookId);
        List<BookBorrowResponse> bookBorrowResponseList = new ArrayList<>();

        for (BorrowedBooks borrowedBooks : borrowedBooksList) {
            Book book = borrowedBooks.getBook();
            Borrower borrower = borrowedBooks.getBorrower();
            String borrowerName = borrower.getName();
            String borrowerPhone = borrower.getPhoneNumber();
            String bookName = book.getName();

            BookBorrowResponse bookBorrowResponse = new BookBorrowResponse(
                    borrowedBooks.getId(),
                    borrowedBooks.getIssueDate(),
                    borrowerName,
                    borrowerPhone,
                    bookName,
                    borrowedBooks.getReturnDate(),
                    borrowedBooks.getLost()
            );

            bookBorrowResponseList.add(bookBorrowResponse);
        }

        return bookBorrowResponseList;
    }

    public BookBorrowResponse addBookReport(Long bookId,BookBorrowRequest bookBorrowRequest) {
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        Book book = bookServiceImpl.getBookById(bookId);
        Borrower borrower = borrowerDao.findByName(bookBorrowRequest.getBorrowerName());
        if (borrower == null) {
            borrower = new Borrower();
            borrower.setName(bookBorrowRequest.getBorrowerName());
            borrower.setPhoneNumber(bookBorrowRequest.getBorrowerPhone());
            borrower.getBooks().add(book);
            borrowerDao.save(borrower);
        } else {
            BorrowedBooks alreadyBorrowedBook = borrowedBooksDao.findByBorrowerIdAndBookIdAndReturnDateIsNull( borrower.getId(),bookBorrowRequest.getBookId());
            if (alreadyBorrowedBook != null) {
                throw new BookAlreadyIssuedException("This book has already been issued to the borrower");
            }
            borrower.getBooks().add(book);
            borrowerDao.save(borrower);
        }
        borrowedBooks.setBook(book);
        borrowedBooks.setBorrower(borrower);
        borrowedBooks.setIssueDate(bookBorrowRequest.getIssueDate());
        borrowedBooks.setReturnDate(bookBorrowRequest.getReturnDate());
        borrowedBooks.setLost(false);
        BorrowedBooks addedBorrowedBooks = borrowedBooksDao.save(borrowedBooks);
        BookBorrowResponse bookBorrowResponse = new BookBorrowResponse();
        bookBorrowResponse.setId(addedBorrowedBooks.getId());
        bookBorrowResponse.setBookName(book.getName());
        bookBorrowResponse.setBorrowerName(borrower.getName());
        bookBorrowResponse.setBorrowerPhone(borrower.getPhoneNumber());
        bookBorrowResponse.setReturnDate(addedBorrowedBooks.getReturnDate());
        bookBorrowResponse.setIssueDate(addedBorrowedBooks.getIssueDate());
        bookBorrowResponse.setLost(addedBorrowedBooks.getLost());
        return bookBorrowResponse;
    }


    public BookBorrowResponse updateBookReport(Long bookId,Long borrowedId, UpdateBookBorrowRequest updateBookBorrowRequest) throws BorrowedNotFoundException {
        Optional<BorrowedBooks> existingBookReport = borrowedBooksDao.findById(borrowedId);
        if (existingBookReport.isEmpty()) {
            throw new BorrowedNotFoundException(Constants.BORROWED_NOT_FOUND + borrowedId);
        }
        bookId =existingBookReport.get().getBook().getId();
        existingBookReport.get().setReturnDate(updateBookBorrowRequest.getReturnDate());
        existingBookReport.get().setLost(updateBookBorrowRequest.getLost());
        borrowedBooksDao.save(existingBookReport.get());
        BookBorrowResponse bookBorrowResponse =new BookBorrowResponse();
        bookBorrowResponse.setId(existingBookReport.get().getId());
        bookBorrowResponse.setBookName(existingBookReport.get().getBook().getName());
        bookBorrowResponse.setBorrowerName(existingBookReport.get().getBorrower().getName());
        bookBorrowResponse.setBorrowerPhone(existingBookReport.get().getBorrower().getPhoneNumber());
        bookBorrowResponse.setReturnDate(existingBookReport.get().getReturnDate());
        bookBorrowResponse.setIssueDate(existingBookReport.get().getIssueDate());
        bookBorrowResponse.setLost(existingBookReport.get().getLost());
        return bookBorrowResponse;
    }

    public void deleteBookReport(Long id) throws BorrowedNotFoundException {
        Optional<BorrowedBooks> bookReport = borrowedBooksDao.findById(id);
        if (bookReport.isEmpty()) {
            throw new BorrowedNotFoundException(Constants.BORROWED_NOT_FOUND + id);
        }
        borrowedBooksDao.delete(bookReport.get());
    }

    public List<Borrower> getBorrowersByBookId(Long bookId) {
        List<Borrower> borrowers = borrowedBooksDao.findBorrowersByBookId(bookId);
        for (Borrower borrower : borrowers) {
            borrower.setBooks(null);
        }
        return borrowers;
    }

}
