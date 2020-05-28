package com.simplejourney.securityoauth2client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @Autowired
    OAuth2AuthorizedClientManager authenticationManager;

    @GetMapping("/")
    public ResponseEntity<String> home(HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return ResponseEntity.ok("Hello, world");
    }
}
