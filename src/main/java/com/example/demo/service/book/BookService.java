package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestBook);

    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    boolean checkIfBookExists(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestBook);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    Page<BookDto> findAllBooksByCategoryId(Long id, Pageable pageable);
}
