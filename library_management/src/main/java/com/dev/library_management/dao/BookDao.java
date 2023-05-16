package com.dev.library_management.dao;

import com.dev.library_management.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Long> {

    public Book findByNameAndIsDeleted(String name, Integer isDeleted);

    public Book findByIsbnAndIsDeleted(String isbn, Integer isDeleted);

    List<Book> findAllByIsDeleted(Integer isDeleted);

    default List<Book> findAllInAscendingOrderById() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return findAllByIsDeleted(0, sort);
    }

    List<Book> findAllByIsDeleted(Integer isDeleted, Sort sort);


}
