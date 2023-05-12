package com.dev.library_management.dao;

import com.dev.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Long> {

    public Book findByName(String name);

    public List<Book> findAllByIsDeleted(Integer isDeleted);

    Book findByIsbn(String isbn);
}
