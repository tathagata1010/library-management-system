package com.dev.library_management.dao;

import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;


@EnableJpaRepositories
public interface BorrowedBooksDao extends JpaRepository<BorrowedBooks, Long> {
//    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
//    Book findBookById(@Param("bookId") Long bookId);

    @Query("SELECT bb.borrower FROM BorrowedBooks bb WHERE bb.book.id = :bookId AND bb.returnDate IS NULL")
    List<Borrower> findBorrowersByBookId(@Param("bookId") Long bookId);





}

