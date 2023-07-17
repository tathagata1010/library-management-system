package com.dev.library.model;

import java.math.BigInteger;
import java.util.StringTokenizer;


public class Book {


    private BigInteger id;

    private String name;

    private String author;

    private String category;

    private boolean isDeleted=false;


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




    @Override
    public String toString() {
        StringTokenizer tokenizer = new StringTokenizer(", ");
        StringBuilder sb;
        sb = new StringBuilder("Book{");

        sb.append("id=").append(id).append(tokenizer);
        sb.append("name='").append(name).append('\'').append(tokenizer);
        sb.append("author='").append(author).append('\'').append(tokenizer);
        sb.append("category='").append(category).append('\'').append(tokenizer);
        sb.append("isDeleted=").append(isDeleted).append(tokenizer);
        sb.append('}');

        return sb.toString();
    }
}

