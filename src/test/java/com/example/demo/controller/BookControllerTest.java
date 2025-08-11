package com.example.demo.controller;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("""
            Get page of all existing books
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllBooks_WithoutParams_ReturnPageOfBookDto() throws Exception {
        BookDto bookDto = new BookDto()
                .setId(100L)
                .setAuthor("Shevchenko")
                .setTitle("Maria")
                .setIsbn("123-456-7890")
                .setCategoryIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setDescription("interesting book")
                .setCoverImage("blue");

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> expected = new  PageImpl<>(List.of(bookDto), pageable, 1);

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(PageResponse.class, BookDto.class);

        PageResponse<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), type
        );

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.content.isEmpty());
        Assertions.assertEquals(expected.getTotalElements(), actual.totalElements);
        Assertions.assertEquals(expected.getContent().get(0).getId(), actual.content.get(0).getId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    @DisplayName("""
            Get book by id and return bookDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getBook_ById_ReturnBookDto() throws Exception {
        BookDto expected = new BookDto()
                .setId(100L)
                .setAuthor("Shevchenko")
                .setTitle("Maria")
                .setIsbn("123-456-7890")
                .setCategoryIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setDescription("interesting book")
                .setCoverImage("blue");

        MvcResult result = mockMvc.perform(get("/books/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getIsbn(), actual.getIsbn());
    }

    public static class PageResponse<T> {
        public List<T> content;
        public long totalElements;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("""
            Create book by given params and should return BookDto
            """)
    public void createBook_ByGivenParams_ReturnBookDto() throws Exception {
        CreateBookRequestDto  createBookRequestDto = new CreateBookRequestDto()
                .setAuthor("Shevchenko")
                .setTitle("Maria")
                .setIsbn("123-456-7890")
                .setCategoriesIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setCoverImage("blue");

        BookDto expected = new BookDto()
                .setId(100L)
                .setAuthor("Shevchenko")
                .setTitle("Maria")
                .setIsbn("123-456-7890")
                .setCategoryIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setCoverImage("blue");

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("""
            Delete book by id and should return BookDto
            """)
    public void deleteBook_ById_ReturnBookDto() throws Exception {
        MvcResult result = mockMvc.perform(delete("/books/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("""
            Update book by given params and id and should return BookDto
            """)
    public void updateBook_ByGivenParamsAndId_ReturnBookDto() throws Exception {
        CreateBookRequestDto  createBookRequestDto = new CreateBookRequestDto()
                .setAuthor("Shevchenko")
                .setTitle("Tania")
                .setIsbn("123-456-7890")
                .setCategoriesIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setCoverImage("blue");

        BookDto expected = new BookDto()
                .setId(100L)
                .setAuthor("Shevchenko")
                .setTitle("Tania")
                .setIsbn("123-456-7890")
                .setCategoryIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setCoverImage("blue");

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(put("/books/100")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("""
            Search books by given params and id and should return page of BookDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void searchBooks_WithGivenParams_ReturnPageOfBookDto() throws Exception {
        BookDto bookDto = new BookDto()
                .setId(100L)
                .setAuthor("Shevchenko")
                .setTitle("Maria")
                .setIsbn("123-456-7890")
                .setCategoryIds(Set.of(100L))
                .setPrice(BigDecimal.valueOf(100))
                .setDescription("interesting book")
                .setCoverImage("blue");

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> expected = new  PageImpl<>(List.of(bookDto), pageable, 1);

        MvcResult result = mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(PageResponse.class, BookDto.class);

        PageResponse<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), type
        );

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.content.isEmpty());
        Assertions.assertEquals(expected.getTotalElements(), actual.totalElements);
        Assertions.assertEquals(expected.getContent().get(0).getId(), actual.content.get(0).getId());
    }
}
