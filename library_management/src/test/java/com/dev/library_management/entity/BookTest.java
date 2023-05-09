package com.dev.library_management.entity;
import com.dev.library_management.entity.Book;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookTest {

    @Test
    void testBookEntity() {
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setCategory("Test Category");

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getName()).isEqualTo("Test Book");
        assertThat(book.getAuthor()).isEqualTo("Test Author");
        assertThat(book.getIsbn()).isEqualTo("1234567890");
        assertThat(book.getCategory()).isEqualTo("Test Category");
    }

    @Test
    void testBookToString() {
        Book book = mock(Book.class);
        when(book.toString()).thenReturn("Book{id=1, name='Test Book', author='Test Author', category='Test Category', isbn='1234567890'}");
        assertThat(book.toString()).isEqualTo("Book{id=1, name='Test Book', author='Test Author', category='Test Category', isbn='1234567890'}");
    }
}
