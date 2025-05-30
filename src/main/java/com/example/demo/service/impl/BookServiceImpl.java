package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(CreateBookRequestDto requestBook) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestBook)));
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).get();
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        book.setTitle(requestBook.getTitle());
        book.setPrice(requestBook.getPrice());
        book.setId(id);
        book.setIsbn(requestBook.getIsbn());
        book.setAuthor(requestBook.getAuthor());
        book.setDescription(requestBook.getDescription());
        book.setCoverImage(requestBook.getCoverImage());
        return bookMapper.toDto(bookRepository.save(book));
    }
}
