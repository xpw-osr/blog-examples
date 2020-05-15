package com.simplejourney.securityjwt.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Permission {
    public enum Permissions {
        Create,
        Delete,
        Modify,
        View
    }

    private long id;
    private Permissions permission;
}
