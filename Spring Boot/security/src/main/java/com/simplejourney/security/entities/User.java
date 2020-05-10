package com.simplejourney.security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    private String password;
    private List<Role> roles;
}
