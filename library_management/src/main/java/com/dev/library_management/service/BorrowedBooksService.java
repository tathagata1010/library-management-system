package com.dev.library_management.service;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.dao.BorrowedBooksDao;
import com.dev.library_management.dao.BorrowerDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.exception.BookReportNotFoundException;
import com.dev.library_management.model.BookBorrowRequest;
import com.dev.library_management.model.BookBorrowResponse;
import com.dev.library_management.model.UpdateBookBorrowRequest;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Map<String, Object>> getAllBookReports() {
        List<BorrowedBooks> borrowedBooks = borrowedBooksDao.findAll();
        List<Map<String, Object>> bookDetailsList = new ArrayList<>();
        for (BorrowedBooks borrowedBook : borrowedBooks) {
            Map<String, Object> element = new HashMap<>();
            element.put("id", borrowedBook.getId());
            element.put("issueDate", borrowedBook.getIssueDate());
            element.put("borrowerName", borrowedBook.getBorrower().getName());
            element.put("borrowerPhone", borrowedBook.getBorrower().getPhoneNumber());
            element.put("bookName", borrowedBook.getBook().getName());
            element.put("returnDate", borrowedBook.getReturnDate());
            bookDetailsList.add(element);
        }
        return bookDetailsList;
    }

    public BookBorrowResponse getBookReportById(Long id) throws BookReportNotFoundException {
        Optional<BorrowedBooks> bookReport = borrowedBooksDao.findById(id);
        if (bookReport.isEmpty()) {
            throw new BookReportNotFoundException("Book Report not found with ID: " + id);
        }
        BookBorrowResponse bookBorrowResponse =new BookBorrowResponse();
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
        BorrowedBooks addedBorrowedBooks = borrowedBooksDao.save(borrowedBooks);
        bookBorrowResponse.setBookName(book.getName());
        bookBorrowResponse.setReturnDate(addedBorrowedBooks.getReturnDate());
        bookBorrowResponse.setIssueDate(addedBorrowedBooks.getIssueDate());
        return bookBorrowResponse;
    }

    public BookBorrowResponse updateBookReport(Long id, UpdateBookBorrowRequest returnDate) throws BookReportNotFoundException {
        Optional<BorrowedBooks> existingBookReport = borrowedBooksDao.findById(id);
        if (existingBookReport.isEmpty()) {
            throw new BookReportNotFoundException("Book Report not found with ID: " + id);
        }
        existingBookReport.get().setReturnDate(returnDate.getReturnDate());
        borrowedBooksDao.save(existingBookReport.get());
        BookBorrowResponse bookBorrowResponse =new BookBorrowResponse();
        bookBorrowResponse.setBookName(existingBookReport.get().getBook().getName());
        bookBorrowResponse.setBorrowerName(existingBookReport.get().getBorrower().getName());
        bookBorrowResponse.setBorrowerPhone(existingBookReport.get().getBorrower().getPhoneNumber());
        bookBorrowResponse.setReturnDate(existingBookReport.get().getReturnDate());
        bookBorrowResponse.setIssueDate(existingBookReport.get().getIssueDate());
        return bookBorrowResponse;
    }

    public void deleteBookReport(Long id) throws BookReportNotFoundException {
        Optional<BorrowedBooks> bookReport = borrowedBooksDao.findById(id);
        if (bookReport.isEmpty()) {
            throw new BookReportNotFoundException("Book Report not found with ID: " + id);
        }
        borrowedBooksDao.delete(bookReport.get());
    }
}
