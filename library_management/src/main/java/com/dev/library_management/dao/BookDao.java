package com.dev.library_management.dao;

import com.dev.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends JpaRepository<Book, Long> {
    // additional methods can be added here
    public Book findByName(String name);
}
