package com.example.demo.repository;

import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Find all books by categories ids and return Page of books
            """)
    @Sql(scripts = {
            "classpath:database/books/add-two-books-to-books-table.sql",
            "classpath:database/category/add-two-categories-to-categories-table.sql",
            "classpath:database/booksCategories/add-categories-for-two-books-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/booksCategories/delete-two-categories-from-books-categories-table.sql",
            "classpath:database/category/delete-two-categories-from-categories-table.sql",
            "classpath:database/books/delete-two-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllBooks_ByCategoriesIds_ShouldReturnPageOfBooks() {
        String expectedTitle = "Maria";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> actual = bookRepository.findAllBooksByCategories_Id(1L, pageable);

        assertEquals(1, actual.getContent().size());
        assertEquals(expectedTitle, actual.getContent().get(0).getTitle());

    }
}
