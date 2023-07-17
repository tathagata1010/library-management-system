package com.dev.library.model;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

 class BookTest {

    @Test
    void testBookEntity() {
        // Create a new instance of Book
        Book book = new Book();
        book.setId(BigInteger.valueOf(1));
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setCategory("Test Category");

        // Assert the properties of the book
        assertThat(book.getId()).isEqualTo(BigInteger.valueOf(1));
        assertThat(book.getName()).isEqualTo("Test Book");
        assertThat(book.getAuthor()).isEqualTo("Test Author");
        assertThat(book.getCategory()).isEqualTo("Test Category");
    }


}
