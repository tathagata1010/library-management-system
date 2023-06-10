package com.dev.library.entity;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Borrower {
    private BigInteger id;

    private String name;

    private String phoneNumber;

    private String walletAddress;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    private Set<Book> books = new HashSet<>();


    public Borrower() {
    }


    public Borrower(BigInteger id, String name, String phoneNumber, String walletAddress, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.walletAddress = walletAddress;
        this.books = books;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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
