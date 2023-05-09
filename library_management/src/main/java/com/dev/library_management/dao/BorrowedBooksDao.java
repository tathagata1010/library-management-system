package com.dev.library_management.dao;

import com.dev.library_management.entity.Book;
import com.dev.library_management.entity.BorrowedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;


@EnableJpaRepositories
public interface BorrowedBooksDao extends JpaRepository<BorrowedBooks, Long> {
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Book findBookById(@Param("bookId") Long bookId);


}

