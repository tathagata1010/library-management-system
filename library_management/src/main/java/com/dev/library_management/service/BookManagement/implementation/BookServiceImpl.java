package com.dev.library_management.service.BookManagement.implementation;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.exception.BookAlreadyExistsException;
import com.dev.library_management.exception.BookNotFoundException;
import com.dev.library_management.service.BookManagement.BookService;
import com.dev.library_management.utility.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {


    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    public Book getBookById(Long id) throws BookNotFoundException {
        return bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException(Constants.BOOK_NOT_FOUND + id));
    }

    public Book getBookByName(String name) throws BookNotFoundException {
        Book book= bookDao.findByNameAndIsDeleted(name,0);

        if (book!=null) {
            return book;
        } else {
            throw new BookNotFoundException(Constants.BOOK_NOT_FOUND_NAME + name);
        }
    }

    public Book addBook(Book book) throws BookAlreadyExistsException {
        Book existingBook = bookDao.findByIsbnAndIsDeleted(book.getIsbn(),0);
        if (existingBook != null) {
            throw new BookAlreadyExistsException("Book with ISBN " + book.getIsbn() + " already exists!");
        }
        return bookDao.save(book);
    }



    public Book updateBook(Long id, Book bookDetails) throws BookNotFoundException {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException(Constants.BOOK_NOT_FOUND + id));

        book.setName(bookDetails.getName());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setCategory(bookDetails.getCategory());

        return bookDao.save(book);
    }

    public void deleteBook(Long id) throws BookNotFoundException {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException(Constants.BOOK_NOT_FOUND + id));

        if (book.getIsDeleted() == 1) {
            return;
        }

        book.setIsDeleted(1);
        bookDao.save(book);
    }

}

