package com.dev.library_management.service.BookManagement;

import com.dev.library_management.entity.Book;
import com.dev.library_management.exception.BookAlreadyExistsException;
import com.dev.library_management.exception.BookNotFoundException;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(Long id) throws BookNotFoundException;

    Book getBookByName(String name) throws BookNotFoundException;

    Book addBook(Book book) throws BookAlreadyExistsException;

    Book updateBook(Long id, Book bookDetails) throws BookNotFoundException;

    void deleteBook(Long id) throws BookNotFoundException;
}
