package com.simplejourney.securityoauth2res.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @GetMapping("/list")
    public ResponseEntity<List<String>> list() {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
