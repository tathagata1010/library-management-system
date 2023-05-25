package com.dev.library.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class BookTest {

    @Test
    void testBookEntity() {
        // Create a new instance of Book
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setCategory("Test Category");

        // Assert the properties of the book
        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getName()).isEqualTo("Test Book");
        assertThat(book.getAuthor()).isEqualTo("Test Author");
        assertThat(book.getIsbn()).isEqualTo("1234567890");
        assertThat(book.getCategory()).isEqualTo("Test Category");
    }

    @Test
    void testBookToString() {
        // Create a new instance of Book
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setCategory("Test Category");

        // Assert the toString representation of the book
        String expectedToString = "Book{id=1, name='Test Book', author='Test Author', category='Test Category', isbn='1234567890', isDeleted=0}";
        assertThat(book.toString()).isEqualTo(expectedToString);
    }
}
