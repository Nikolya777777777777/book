package com.example.demo.repository;

import com.example.demo.dto.book.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Category category = new Category()
                .setName("Detective")
                .setId(1L)
                .setDescription("about spies");
        Book book = new Book()
                .setId(1L)
                .setAuthor("Shevchenko")
                .setTitle("Maria")
                .setCategories(Set.of(category))
                .setIsbn("123")
                .setPrice(BigDecimal.valueOf(999))
                .setDescription("interesting book")
                .setCoverImage("blue");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> expected = new PageImpl<>(List.of(book), pageable, 1);
        Page<Book> actual = bookRepository.findAllBooksByCategories_Id(1L, pageable);

        Book actualBook = actual.getContent().get(0);
        Book expectedBook = expected.getContent().get(0);

        actualBook.setCategories(new HashSet<>(actualBook.getCategories()));
        expectedBook.setCategories(new HashSet<>(expectedBook.getCategories()));

        expected.getContent().get(0).setPrice(expected.getContent().get(0).getPrice().setScale(2));
        actual.getContent().get(0).setPrice(actual.getContent().get(0).getPrice().setScale(2));

        Set<Long> actualCategoryIds = actualBook.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());

        Set<Long> expectedCategoryIds = expectedBook.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());


        assertTrue(EqualsBuilder.reflectionEquals(expectedBook, actualBook, "categories"));
        assertEquals(actualCategoryIds, expectedCategoryIds);
    }
}
