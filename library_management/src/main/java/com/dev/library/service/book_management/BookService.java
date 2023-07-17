package com.dev.library.service.book_management;

import com.dev.library.model.Book;
import com.dev.library.exception.BookNotFoundException;

import java.math.BigInteger;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks() throws Exception;

    Book getBookByName(String name) throws BookNotFoundException;

    Book addBook(Book book) throws Exception;

    Book updateBook(BigInteger id, Book bookDetails) throws BookNotFoundException;

    void deleteBook(BigInteger id) throws BookNotFoundException;
}
