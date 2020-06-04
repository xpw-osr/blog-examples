package com.simplejourney.securityacl.config;

import org.springframework.security.acls.domain.BasePermission;

public class DemoBasePermission extends BasePermission {
    public DemoBasePermission(int mask) {
        super(mask);
    }
}
