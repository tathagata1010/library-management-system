package com.dev.library.dao;

import com.dev.library.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookDao extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.name = :name AND b.isDeleted = :isDeleted")
    Book findByNameAndIsDeleted(@Param("name") String name, @Param("isDeleted") Integer isDeleted);

    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn AND b.isDeleted = :isDeleted")
    Book findByIsbnAndIsDeleted(@Param("isbn") String isbn, @Param("isDeleted") Integer isDeleted);


    @Query("SELECT b FROM Book b WHERE b.isDeleted = :isDeleted")
    List<Book> findAllByIsDeleted(Integer isDeleted);

    default List<Book> findAllInAscendingOrderById() {
        return findAllByIsDeleted(0, Sort.by(Sort.Direction.ASC, "id"));
    }

    List<Book> findAllByIsDeleted(Integer isDeleted, Sort sort);


}
