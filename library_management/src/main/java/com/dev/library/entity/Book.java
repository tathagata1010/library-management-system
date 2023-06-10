package com.dev.library.entity;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.util.List;


public class Book {


    private BigInteger id;

    private String name;

    private String author;

    private String category;

    private boolean isDeleted=false;

//    private List<String> borrower;

    // Constructor
    public Book() {
    }


    public Book(BigInteger id, String name, String author, String category, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category = category;
        this.isDeleted = isDeleted;
      }

    // Getters and setters for all fields
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }



//    public List<String> getBorrower() {
//        return borrower;
//    }
//
//    public void setBorrower(List<String> borrower) {
//        this.borrower = borrower;
//    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

