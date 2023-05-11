package com.dev.library_management.service;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.dao.BorrowedBooksDao;
import com.dev.library_management.dao.BorrowerDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.exception.BorrowedNotFoundException;
import com.dev.library_management.model.BookBorrowRequest;
import com.dev.library_management.model.BookBorrowResponse;
import com.dev.library_management.model.UpdateBookBorrowRequest;
import com.dev.library_management.utility.Constants;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BorrowedBooksService {

    private final BorrowedBooksDao borrowedBooksDao;
    private final BookDao bookDao;
    private final BorrowerDao borrowerDao;
    private BookService bookService;
    public BorrowedBooksService(BorrowedBooksDao borrowedBooksDao, BookService bookService, BookDao bookDao, BorrowerDao borrowerDao) {
        this.borrowedBooksDao = borrowedBooksDao;
        this.bookDao = bookDao;
        this.bookService = bookService;
        this.borrowerDao = borrowerDao;
    }

    public List<BookBorrowResponse> getAllBookReports() {
        List<BorrowedBooks> borrowedBooks = borrowedBooksDao.findAll();
        List<BookBorrowResponse> bookDetailsList = new ArrayList<>();
        for (BorrowedBooks borrowedBook : borrowedBooks) {
            BookBorrowResponse bookBorrowResponse=new BookBorrowResponse();
            bookBorrowResponse.setId(borrowedBook.getId());
            bookBorrowResponse.setIssueDate(borrowedBook.getIssueDate());
            bookBorrowResponse.setBorrowerName(borrowedBook.getBorrower().getName());
            bookBorrowResponse.setBorrowerPhone(borrowedBook.getBorrower().getPhoneNumber());
            bookBorrowResponse.setBookName(borrowedBook.getBook().getName());
            bookBorrowResponse.setReturnDate(borrowedBook.getReturnDate());
            bookBorrowResponse.setLost(borrowedBook.getLost());
            bookDetailsList.add(bookBorrowResponse);
        }
        return bookDetailsList;
    }

    public BookBorrowResponse getBookReportById(Long id) throws BorrowedNotFoundException {
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
    }

    public BookBorrowResponse addBookReport(BookBorrowRequest bookBorrowRequest) {
        BorrowedBooks borrowedBooks = new BorrowedBooks();
        bookService = new BookService(bookDao);
        Book book= bookService.getBookById(bookBorrowRequest.getBookId());
        borrowedBooks.setBook(book);
        Borrower borrower=borrowerDao.findByName(bookBorrowRequest.getBorrowerName());
        BookBorrowResponse bookBorrowResponse =new BookBorrowResponse();
        if(borrower==null)
        {
            Borrower borrower1=new Borrower();
            borrower1.setName(bookBorrowRequest.getBorrowerName());
            borrower1.setPhoneNumber(bookBorrowRequest.getBorrowerPhone());
            borrower1.getBooks().add(book);
            borrowerDao.save(borrower1);
            bookBorrowResponse.setBorrowerName(borrower1.getName());
            bookBorrowResponse.setBorrowerPhone(borrower1.getPhoneNumber());
            borrowedBooks.setBorrower(borrower1);
        }
        else {
            borrower.getBooks().add(book);
            borrowerDao.save(borrower);
            bookBorrowResponse.setBorrowerName(borrower.getName());
            bookBorrowResponse.setBorrowerPhone(borrower.getPhoneNumber());
            borrowedBooks.setBorrower(borrower);
        }
        borrowedBooks.setIssueDate(bookBorrowRequest.getIssueDate());
        borrowedBooks.setReturnDate(bookBorrowRequest.getReturnDate());
        borrowedBooks.setLost(false);
        BorrowedBooks addedBorrowedBooks = borrowedBooksDao.save(borrowedBooks);
        bookBorrowResponse.setId(addedBorrowedBooks.getId());
        bookBorrowResponse.setBookName(book.getName());
        bookBorrowResponse.setReturnDate(addedBorrowedBooks.getReturnDate());
        bookBorrowResponse.setIssueDate(addedBorrowedBooks.getIssueDate());
        return bookBorrowResponse;
    }

    public BookBorrowResponse updateBookReport(Long id, UpdateBookBorrowRequest updateBookBorrowRequest) throws BorrowedNotFoundException {
        Optional<BorrowedBooks> existingBookReport = borrowedBooksDao.findById(id);
        if (existingBookReport.isEmpty()) {
            throw new BorrowedNotFoundException(Constants.BORROWED_NOT_FOUND + id);
        }
        existingBookReport.get().setReturnDate(updateBookBorrowRequest.getReturnDate());
        existingBookReport.get().setLost(updateBookBorrowRequest.getLost());

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
