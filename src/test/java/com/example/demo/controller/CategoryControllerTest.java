package com.example.demo.controller;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
            Search books by given params and category id and should return page of BookDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllBooks_ByCategoryId_ReturnPageOfBookDto() throws Exception {
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
        Page<BookDto> expected = new PageImpl<>(List.of(bookDto), pageable, 1);

        MvcResult result = mockMvc.perform(get("/categories/100/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CategoryControllerTest.PageResponse.class, BookDto.class);

        CategoryControllerTest.PageResponse<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), type
        );

        expected.getContent().get(0).setPrice(expected.getContent().get(0).getPrice().setScale(2));
        actual.content.get(0).setPrice(actual.content.get(0).getPrice().setScale(2));

        assertNotNull(actual);
        assertFalse(actual.content.isEmpty());
        assertEquals(expected.getTotalElements(), actual.totalElements);
        assertTrue(EqualsBuilder.reflectionEquals(expected.getContent().get(0), actual.content.get(0)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("""
            Get category by id and should return CategoryResponseDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getCategory_ById_ReturnCategoryResponseDto() throws Exception {
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(100L)
                .setName("Detective")
                .setDescription("about spies");

        MvcResult result = mockMvc.perform(get("/categories/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("""
            Get category by id and should return CategoryResponseDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllCategories_ByGivenPageable_ReturnPageOfCategoryResponseDto() throws Exception {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
                .setId(100L)
                .setName("Detective")
                .setDescription("about spies");

        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryResponseDto> expected = new PageImpl<>(List.of(categoryResponseDto), pageable, 1);

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CategoryControllerTest.PageResponse.class, CategoryResponseDto.class);

        PageResponse<CategoryResponseDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(), type);

        assertNotNull(actual);
        assertEquals(expected.getTotalElements(), actual.totalElements);
        assertTrue(EqualsBuilder.reflectionEquals(expected.getContent().get(0), actual.content.get(0)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Create category by given params and should return CategoryResponseDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createCategories_ByGivenParams_ReturnCategoryResponseDto() throws Exception {
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(1L)
                .setName("Detective")
                .setDescription("about spies");

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("Detective")
                .setDescription("about spies");

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Update category by given params and id and should return CategoryResponseDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateCategory_ByGivenParamsAndId_ReturnCategoryResponseDto() throws Exception {
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(100L)
                .setName("Comedy")
                .setDescription("funny stories");

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("Comedy")
                .setDescription("funny stories");

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/100")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Delete category by given params and id and should return CategoryResponseDto
            """)
    @Sql(scripts = {
            "classpath:database/truncate/truncate-all-tables.sql",
            "classpath:database/controller/category/add-category-to-category-table.sql",
            "classpath:database/controller/book/add-new-book-to-book-table.sql",
            "classpath:database/controller/booksCategories/add-books-categories-record-into-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteCategories_ByGivenParamsAndId_ReturnStatus() throws Exception {
        MvcResult result = mockMvc.perform(delete("/categories/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    public static class PageResponse<T> {
        public List<T> content;
        public long totalElements;
    }
}
