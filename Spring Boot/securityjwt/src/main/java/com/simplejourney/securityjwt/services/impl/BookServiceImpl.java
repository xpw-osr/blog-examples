package com.simplejourney.securityjwt.services.impl;

import com.google.common.collect.Lists;
import com.simplejourney.securityjwt.dto.Book;
import com.simplejourney.securityjwt.services.BookService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
    private Map<Long, Book> books = new HashMap<Long, Book>() {{
        put(new Long(0), new Book(0, "Charlotte's web", "Friendship of Charlotte and Web"));
        put(new Long(1), new Book(1, "Animal Farm", "Revolution of animal in a farm"));
        put(new Long(2), new Book(2, "The old man and the Sea", "A story of an old man and a fish"));
    }};

    @Override
    public void add(Book book) {
        long newId = books.size();
        books.put(new Long(newId), new Book(newId, book.getName(), book.getDescription()));
    }

    @Override
    public void delete(long id) {
        books.remove(id);
    }

    @Override
    public Book update(Book book) {
        books.put(book.getId(), book);
        return books.get(book.getId());
    }

    @Override
    public List<Book> findAll() {
        return Lists.newArrayList(books.values());
    }
}
