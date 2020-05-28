package com.simplejourney.securityoauth2auth.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Role {
    public enum Roles {
        Admin("ROLE_ADMIN"),
        Vistor("ROLE_VISITOR");

        private String name;
        Roles(String name) {
            this.name = name;
        }
    }

    private long id;
    private Roles role;
    private List<Permission> permissions;
}
