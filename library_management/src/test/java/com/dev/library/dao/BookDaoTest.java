package com.dev.library.dao;

import com.dev.library.entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan(basePackages = "com.dev.library.entity")
@EnableJpaRepositories(basePackages = "com.dev.library.dao")
@ComponentScan(
        basePackages = "com.dev.library.dao",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BookDao.class)
)
@TestPropertySource(locations = "classpath:application-test.properties")
class BookDaoTest {

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        bookDao.deleteAll();

        // Instantiate and save books
        Book book1 = new Book();
        book1.setName("Book 1");
        book1.setAuthor("Author 1");
        book1.setCategory("Category 1");
        book1.setIsbn("100");
        book1.setIsDeleted(0);
        bookDao.save(book1);

        Book book2 = new Book();
        book2.setName("Book 2");
        book2.setAuthor("Author 2");
        book2.setCategory("Category 2");
        book2.setIsbn("200");
        book2.setIsDeleted(0);
        bookDao.save(book2);
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setName("New Book");
        book.setAuthor("Author");
        book.setCategory("Category");
        book.setIsbn("300");
        book.setIsDeleted(0);

        Book savedBook = bookDao.save(book);

        Assertions.assertNotNull(savedBook.getId());
    }

    @Test
    void testFindById() {
        List<Book> books = bookDao.findAll();
        Book book = books.get(0);

        Optional<Book> foundBookOptional = bookDao.findById(book.getId());

        Assertions.assertTrue(foundBookOptional.isPresent());
        Book foundBook = foundBookOptional.get();
        Assertions.assertEquals(book.getName(), foundBook.getName());
        Assertions.assertEquals(book.getAuthor(), foundBook.getAuthor());
        Assertions.assertEquals(book.getCategory(), foundBook.getCategory());
        Assertions.assertEquals(book.getIsbn(), foundBook.getIsbn());
        Assertions.assertEquals(book.getIsDeleted(), foundBook.getIsDeleted());
    }

    @Test
    void testFindAllInAscendingOrderById() {
        List<Book> books = bookDao.findAllInAscendingOrderById();

        Assertions.assertEquals(2, books.size());
        Assertions.assertEquals("Book 1", books.get(0).getName());
        Assertions.assertEquals("Book 2", books.get(1).getName());
    }

    @Test
    void testFindByIsbnAndIsDeleted() {
        Book book = bookDao.findByIsbnAndIsDeleted("200", 0);

        Assertions.assertEquals("Book 2", book.getName());
        Assertions.assertEquals("Author 2", book.getAuthor());
    }
}
