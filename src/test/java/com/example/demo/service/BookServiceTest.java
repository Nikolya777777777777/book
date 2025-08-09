package com.example.demo.service;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.book.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.service.book.impl.BookServiceImpl;
import com.example.demo.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	@Mock
	private BookRepository bookRepository;

	@Mock
	private BookMapper bookMapper;

	@Mock
	private BookSpecificationBuilder bookSpecificationBuilder;

	@Mock
	private CategoryServiceImpl categoryService;

	@InjectMocks
	private BookServiceImpl bookService;
	@Test
	@DisplayName("""
			Create new book
			""")
	public void createBook_WithValidRequest_ReturnBookDto() {
		Book book = new Book()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategories(Set.of(new Category(1L)))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		CreateBookRequestDto requestBook = new CreateBookRequestDto()
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoriesIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		BookDto bookDto = new BookDto()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoryIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		when(bookRepository.save(book)).thenReturn(book);
		when(bookMapper.toModel(requestBook)).thenReturn(book);
		when(bookMapper.toDto(book)).thenReturn(bookDto);

		BookDto savedBookDto = bookService.createBook(requestBook);

		assertThat(savedBookDto).isEqualTo(bookDto);
		verify(bookRepository, times(1)).save(book);
		verify(bookMapper, times(1)).toModel(requestBook);
		verify(bookMapper, times(1)).toDto(book);
		verifyNoMoreInteractions(bookRepository,  bookMapper);
	}

	@Test
	@DisplayName("""
			Get all books
			""")
	public void getAllBooks_WithValidPageObject_ReturnPageOfBookDto() {
		Book book1 = new Book()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategories(Set.of(new Category(1L)))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		BookDto bookDto1 = new BookDto()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoryIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		Book book2 = new Book()
				.setId(2L)
				.setAuthor("Shekspir")
				.setCoverImage("12345")
				.setTitle("Monaliza")
				.setIsbn("12312356")
				.setCategories(Set.of(new Category(2L)))
				.setDescription("Dramatic story")
				.setPrice(BigDecimal.valueOf(901));

		BookDto bookDto2 = new BookDto()
				.setId(2L)
				.setAuthor("Shekspir")
				.setCoverImage("12345")
				.setTitle("Monaliza")
				.setIsbn("12312356")
				.setCategoryIds(Set.of(2L))
				.setDescription("Dramatic story")
				.setPrice(BigDecimal.valueOf(901));

		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> bookPage = new PageImpl<>(List.of(book1, book2), pageable, 2);

		when(bookMapper.toDto(book1)).thenReturn(bookDto1);
		when(bookMapper.toDto(book2)).thenReturn(bookDto2);
		when(bookRepository.findAll(pageable)).thenReturn(bookPage);

		Page<BookDto> actual = bookService.getAll(pageable);

		assertEquals(2, actual.getTotalElements());
		assertThat(actual.getContent().get(0).getId()).isEqualTo(1L);
		assertThat(actual.getContent().get(1).getId()).isEqualTo(2L);

		verify(bookRepository, times(1)).findAll(pageable);
		verify(bookMapper, times(1)).toDto(book1);
		verify(bookMapper, times(1)).toDto(book2);
		verifyNoMoreInteractions(bookRepository,  bookMapper);
	}

	@Test
	@DisplayName("""
			Get book by id
			""")
	public void getBookById_WithValidId_ReturnBookDto() {
		Book book = new Book()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategories(Set.of(new Category(1L)))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		BookDto bookDto = new BookDto()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoryIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
		when(bookMapper.toDto(book)).thenReturn(bookDto);

		BookDto actual = bookService.getBookById(book.getId());

		assertThat(actual).isEqualTo(bookDto);
		verify(bookRepository, times(1)).findById(book.getId());
		verify(bookMapper, times(1)).toDto(book);
		verifyNoMoreInteractions(bookRepository,  bookMapper);
	}

	@Test
	@DisplayName("""
			Get book with invalid id and waiting for exception
			""")
	public void getBookById_WithInvalidValidId_ReturnBookDto() {
		Long bookId = 100L;

		String expected = "Can not find book with id: " + bookId;

		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(
				RuntimeException.class,
				() -> bookService.getBookById(bookId)
		);

		assertThat(exception.getMessage()).isEqualTo(expected);
		verify(bookRepository, times(1)).findById(bookId);
		verifyNoMoreInteractions(bookRepository);
	}

	@Test
	@DisplayName("""
			Delete book with valid id
			""")
	public void deleteBookById_WithValidId_ShouldCallRepositoryDeleteById() {
		Long bookId = 1L;

		bookService.deleteById(bookId);

		verify(bookRepository, times(1)).deleteById(bookId);
		verifyNoMoreInteractions(bookRepository);
	}

	@Test
	@DisplayName("""
			Update book by id
			""")
	public void updateBookById_WithValidRequest_ReturnBookDto() {
		Book book = new Book()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategories(Set.of(new Category(1L)))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		BookDto bookDto = new BookDto()
				.setId(1L)
				.setAuthor("Grygor")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoryIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		CreateBookRequestDto requestDto = new CreateBookRequestDto()
				.setAuthor("Grygor")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoriesIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
		when(bookMapper.updateBookFromDto(requestDto, book)).thenReturn(book.setAuthor("Grygor"));
		when(bookRepository.save(book)).thenReturn(book);
		when(bookMapper.toDto(book)).thenReturn(bookDto);

		BookDto actual = bookService.updateBook(book.getId(), requestDto);

		assertThat(actual.getAuthor()).isEqualTo(requestDto.getAuthor());
		verify(bookRepository, times(1)).findById(book.getId());
		verify(bookMapper, times(1)).updateBookFromDto(requestDto, book);
		verify(bookMapper, times(1)).toDto(book);
		verify(bookRepository, times(1)).save(book);
		verifyNoMoreInteractions(bookRepository,  bookMapper);
	}

	@Test
	@DisplayName("""
			Update book with wrong id and we expect to throw exception
			""")
	public void updateBookById_WithInValidId_ReturnPageOfBookDto() {
		Long bookId = 100L;

		CreateBookRequestDto requestDto = new CreateBookRequestDto()
				.setAuthor("Grygor")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoriesIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		String expected = "Book with id: " + bookId + " was not found";

		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(
				RuntimeException.class,
				() -> bookService.updateBook(bookId, requestDto)
		);

		assertThat(exception.getMessage()).isEqualTo(expected);
		verify(bookRepository, times(1)).findById(bookId);
		verifyNoMoreInteractions(bookRepository,  bookMapper);
	}

	@Test
	@DisplayName("""
			Search books with given params and sort by pageable
			""")
	public void searchAllBooks_WithParams_ShouldReturnPageOfBookDto() {
		BookSearchParametersDto params = new BookSearchParametersDto(new String[]{"Maria"}, new String[]{"Shevchenko"}, new String[]{"123123"});

		Book book1 = new Book()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategories(Set.of(new Category(1L)))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		BookDto bookDto = new BookDto()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoryIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		Specification<Book> specificationBuilder = bookSpecificationBuilder.build(params);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> page = new PageImpl<>(List.of(book1), pageable, 1);

		when(bookRepository.findAll(specificationBuilder, pageable)).thenReturn(page);
		when(bookMapper.toDto(book1)).thenReturn(bookDto);

		Page<BookDto> actual = bookService.search(params, pageable);

		assertThat(actual.getContent().size()).isEqualTo(1);
		assertThat(actual.getContent().get(0).getTitle()).isEqualTo(book1.getTitle());
		verify(bookRepository, times(1)).findAll(specificationBuilder, pageable);
		verifyNoMoreInteractions(bookRepository,  bookMapper);
	}

	@Test
	@DisplayName("""
			Find all books by category id and should return Page of BookDto
			""")
	public void findAllBooks_ByCategoryId_ReturnPageOfBookDto() {
		BookSearchParametersDto params = new BookSearchParametersDto(new String[]{"Maria"}, new String[]{"Shevchenko"}, new String[]{"123123"});

		Book book = new Book()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategories(Set.of(new Category(1L)))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		BookDto bookDto = new BookDto()
				.setId(1L)
				.setAuthor("Shevchenko")
				.setCoverImage("123")
				.setTitle("Maria")
				.setIsbn("123123")
				.setCategoryIds(Set.of(1L))
				.setDescription("Interesting book")
				.setPrice(BigDecimal.valueOf(999));

		CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
				.setId(1L)
				.setName("Detective")
				.setDescription("Interesting satisfaction stories about spies and good boys");

		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);

		when(categoryService.findCategoryById(categoryResponseDto.getId())).thenReturn(categoryResponseDto);
		when(bookRepository.findAllBooksByCategories_Id(categoryResponseDto.getId(), pageable)).thenReturn(page);
		when(bookMapper.toDto(book)).thenReturn(bookDto);

		Page<BookDto> actual = bookService.findAllBooksByCategoryId(categoryResponseDto.getId(), pageable);

		assertThat(actual.getContent().size()).isEqualTo(1);
		assertThat(actual.getContent().get(0).getTitle()).isEqualTo(book.getTitle());
		verify(bookRepository, times(1)).findAllBooksByCategories_Id(categoryResponseDto.getId(), pageable);
		verify(bookMapper, times(1)).toDto(book);
		verify(categoryService, times(1)).findCategoryById(categoryResponseDto.getId());
		verifyNoMoreInteractions(bookRepository,  bookMapper, categoryService);
	}

	@Test
	@DisplayName("""
			Search book with wrong category id and we expect to throw exception
			""")
	public void findAllBooks_WithInvalidCategoryId_ShouldThrowException() {
		Long categoryId = 2L;
		Pageable pageable = PageRequest.of(0, 10);

		String expectedMessage = "Category not found by id: " + categoryId;

		when(categoryService.findCategoryById(categoryId))
				.thenThrow(new EntityNotFoundException("Category not found by id: " + categoryId));

		Exception exception = assertThrows(
				EntityNotFoundException.class,
				() -> bookService.findAllBooksByCategoryId(categoryId, pageable)
		);

		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

}
