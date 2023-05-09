package com.dev.library_management.dao;


import com.dev.library_management.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerDao extends JpaRepository<Borrower, Long> {

    Borrower findByName(String name);
}
