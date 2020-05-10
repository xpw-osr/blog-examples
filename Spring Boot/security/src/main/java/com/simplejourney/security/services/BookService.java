package com.simplejourney.security.services;

import com.simplejourney.security.dto.Book;

import java.util.List;

public interface BookService {
    void add(Book book);
    void delete(long id);
    Book update(Book book);
    List<Book> findAll();
}
