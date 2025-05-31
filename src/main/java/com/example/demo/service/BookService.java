package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestBook);

    List<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestBook);

    List<BookDto> search(BookSearchParametersDto params);
}
