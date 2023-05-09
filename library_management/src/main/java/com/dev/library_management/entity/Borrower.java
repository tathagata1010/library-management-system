package com.dev.library_management.entity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "borrowers")
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "borrowers_books",
            joinColumns = @JoinColumn(name = "borrower_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();


    public Borrower() {
    }

    public Borrower(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
