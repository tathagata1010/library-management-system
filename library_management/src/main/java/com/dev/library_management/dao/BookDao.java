package com.dev.library_management.dao;

import com.dev.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Long> {

    public Book findByNameAndIsDeleted(String name, Integer isDeleted);

    public Book findByIsbnAndIsDeleted(String isbn, Integer isDeleted);

    public List<Book> findAllByIsDeleted(Integer isDeleted);

    public default List<Book> findAll() {
        return findAllByIsDeleted(0);
    }


}
