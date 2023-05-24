package com.simplejourney.springk8s.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Profile {
    @Autowired
    private  Environment env;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public String[] profile() {
        return env.getActiveProfiles();
    }
}
