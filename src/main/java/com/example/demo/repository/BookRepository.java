package com.example.demo2.repository;


import com.example.demo.model.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book);
    List findAll();
}
