package com.simplejourney.security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Permission {
    // #################################################
    // Complex Permission Enum Definition - BEGIN
//    public enum Permissions {
//        Create("PERMISSION_CREATE"),
//        Delete("PERMISSION_DELETE"),
//        Modify("PERMISSION_MODIFY"),
//        View("PERMISSION_VIEW");
//
//        private String code;
//        Permissions(String code) {
//            this.code = code;
//        }
//
//        public String code() {
//            return this.code;
//        }
//    }
    // Complex Permission Enum Definition - END
    // #################################################


    public enum Permissions {
        Create,
        Delete,
        Modify,
        View
    }

    private long id;
    private Permissions permission;
}
