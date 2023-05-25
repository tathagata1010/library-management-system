package com.dev.library.dao;

import com.dev.library.entity.Book;
import com.dev.library.entity.BorrowedBooks;
import com.dev.library.entity.Borrower;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan(basePackages = "com.dev.library.entity")
@EnableJpaRepositories(basePackages = "com.dev.library.dao")
@ComponentScan(
        basePackages = "com.dev.library.dao",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BorrowedBooksDao.class)
)
@TestPropertySource(locations = "classpath:application-test.properties")
class BorrowedBooksDaoTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BorrowedBooksDao borrowedBooksDao;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
//        entityManager.createQuery("DELETE FROM BorrowedBooks").executeUpdate();

        // Instantiate and save borrowed books
        BorrowedBooks borrowedBook1 = new BorrowedBooks();
        borrowedBook1.setIssueDate(LocalDate.now());
        borrowedBook1.setReturnDate(null);

        Book book1 = new Book();
        book1.setName("Book 1");
        book1.setAuthor("Author 1");
        book1.setCategory("Category 1");
        book1.setIsbn("100");
        book1.setIsDeleted(0);
        entityManager.persist(book1);
        borrowedBook1.setBook(book1);

        Borrower borrower1 = new Borrower();
        borrower1.setName("Borrower 1");
        borrower1.setPhoneNumber("12345");
        entityManager.persist(borrower1);
        borrowedBook1.setBorrower(borrower1);

        entityManager.persist(borrowedBook1);

        BorrowedBooks borrowedBook2 = new BorrowedBooks();
        borrowedBook2.setIssueDate(LocalDate.now());
        borrowedBook2.setReturnDate(null);

        Book book2 = new Book();
        book2.setName("Book 2");
        book2.setAuthor("Author 2");
        book2.setCategory("Category 2");
        book2.setIsbn("200");
        book2.setIsDeleted(0);
        entityManager.persist(book2);
        borrowedBook2.setBook(book2);

        Borrower borrower2 = new Borrower();
        borrower2.setName("Borrower 2");
        borrower2.setPhoneNumber("5678");
        entityManager.persist(borrower2);
        borrowedBook2.setBorrower(borrower2);

        entityManager.persist(borrowedBook2);
    }

    @Test
    void testFindBorrowersByBookId() {
        List<BorrowedBooks> borrowedBooksList = borrowedBooksDao.findAll();
        BorrowedBooks borrowedBook = borrowedBooksList.get(0);
        Book book = borrowedBook.getBook();

        List<Borrower> borrowers = borrowedBooksDao.findBorrowersByBookId(book.getId());

        Assertions.assertEquals(1, borrowers.size());
        Assertions.assertEquals("Borrower 1", borrowers.get(0).getName());
    }

    @Test
    void testFindByBorrowerIdAndBookIdAndReturnDateIsNull() {
        List<BorrowedBooks> borrowedBooksList = borrowedBooksDao.findAll();
        BorrowedBooks borrowedBook = borrowedBooksList.get(0);
        Borrower borrower = borrowedBook.getBorrower();
        Book book = borrowedBook.getBook();

        BorrowedBooks foundBorrowedBook = borrowedBooksDao.findByBorrowerIdAndBookIdAndReturnDateIsNull(borrower.getId(), book.getId());

        Assertions.assertNotNull(foundBorrowedBook);
        Assertions.assertEquals(borrower.getId(), foundBorrowedBook.getBorrower().getId());
        Assertions.assertEquals(book.getId(), foundBorrowedBook.getBook().getId());
        Assertions.assertNull(foundBorrowedBook.getReturnDate());
    }

    @Test
    void testFindByBookId() {
        List<BorrowedBooks> borrowedBooksList = borrowedBooksDao.findAll();
        BorrowedBooks borrowedBook = borrowedBooksList.get(0);
        Book book = borrowedBook.getBook();

        List<BorrowedBooks> foundBorrowedBooks = borrowedBooksDao.findByBookId(book.getId());

        Assertions.assertEquals(1, foundBorrowedBooks.size());
        Assertions.assertEquals(book.getId(), foundBorrowedBooks.get(0).getBook().getId());
    }

    @Test
    void testFindAllByOrderByIssueDateAsc() {
        List<BorrowedBooks> borrowedBooksList = borrowedBooksDao.findAllByOrderByIssueDateAsc();

        Assertions.assertEquals(2, borrowedBooksList.size());
        Assertions.assertEquals("Book 1", borrowedBooksList.get(0).getBook().getName());
        Assertions.assertEquals("Book 2", borrowedBooksList.get(1).getBook().getName());
    }
}
