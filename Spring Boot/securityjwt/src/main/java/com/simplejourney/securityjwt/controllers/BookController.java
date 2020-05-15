package com.simplejourney.securityjwt.controllers;

import com.simplejourney.securityjwt.dto.Book;
import com.simplejourney.securityjwt.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PreAuthorize("hasAuthority('Create')")  // If use custom access decision, remove it
    @PostMapping()
    public ResponseEntity add(@RequestBody Book book) {
        bookService.add(book);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('Delete')")  // If use custom access decision, remove it
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('Modify')")  // If use custom access decision, remove it
    @PutMapping()
    public ResponseEntity<Book> update(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.update(book));
    }

    @PreAuthorize("hasAuthority('View')")  // If use custom access decision, remove it
    @GetMapping()
    public ResponseEntity<List<Book>> listAll() {
        return ResponseEntity.ok(bookService.findAll());
    }
}
