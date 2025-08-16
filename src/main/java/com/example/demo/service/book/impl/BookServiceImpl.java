package com.example.demo.service.book.impl;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.mapper.book.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.service.book.BookService;
import com.example.demo.service.category.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryService categoryService;

    @Override
    public BookDto createBook(CreateBookRequestDto requestBook) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestBook)));
    }

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can not find book with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: "
                        + id + " was not found"));
        book = bookMapper.updateBookFromDto(requestBook, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public Page<BookDto> findAllBooksByCategoryId(Long id, Pageable pageable) {
        CategoryResponseDto categoryResponseDto = categoryService.findCategoryById(id);
        Page<Book> books = bookRepository
                .findAllBooksByCategories_Id(categoryResponseDto.getId(), pageable);
        return books.map(bookMapper::toDto);
    }
}
