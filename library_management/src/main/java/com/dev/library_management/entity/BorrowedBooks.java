package com.dev.library_management.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBooks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", referencedColumnName = "id", nullable = false)
    private Borrower borrower;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "is_lost")
    private Boolean isLost;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getLost() {
        return isLost;
    }

    public void setLost(Boolean lost) {
        isLost = lost;
    }

    // constructor
    public BorrowedBooks() {
    }


    public BorrowedBooks(Long id, Book book, Borrower borrower, LocalDate issueDate, LocalDate returnDate, Boolean isLost) {
        this.id = id;
        this.book = book;
        this.borrower = borrower;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.isLost = isLost;
    }

    @Override
    public String toString() {
        return "BorrowedBooks{" +
                "id=" + id +
                ", book=" + book +
                ", borrower=" + borrower +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                ", isLost=" + isLost +
                '}';
    }
}
