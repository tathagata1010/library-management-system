package com.dev.library.dao;


import com.dev.library.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerDao extends JpaRepository<Borrower, Long> {

    Borrower findByName(String name);
}
