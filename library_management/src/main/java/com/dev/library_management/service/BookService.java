package com.dev.library_management.service;

import com.dev.library_management.dao.BookDao;
import com.dev.library_management.entity.Book;
import com.dev.library_management.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {


    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    public Book getBookById(Long id) throws BookNotFoundException {
        return bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    }

    public Book getBookByName(String name) throws BookNotFoundException {
        Book book= bookDao.findByName(name);

        if (book!=null) {
            return book;
        } else {
            throw new BookNotFoundException("Book not found with name " + name);
        }
    }

    public Book addBook(Book book) {
        return bookDao.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) throws BookNotFoundException {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));

        book.setName(bookDetails.getName());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setCategory(bookDetails.getCategory());

        return bookDao.save(book);
    }

    public void deleteBook(Long id) throws BookNotFoundException {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));

        bookDao.delete(book);
    }
}

