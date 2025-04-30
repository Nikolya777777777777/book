package com.example.demo.repository;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import java.util.List;

public interface BookRepository {
    Book createBook(Book book);

    List<Book> getAll();

    BookDto getBookById(Long id);
}
