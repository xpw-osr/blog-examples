package com.simplejourney.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * If you add code to redirect after authentication succeed, you need create this controller.
 * Otherwise, need not.
 */

@Controller
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/login")
    public ResponseEntity<Boolean> login() {
        return ResponseEntity.ok(true);
    }
}
