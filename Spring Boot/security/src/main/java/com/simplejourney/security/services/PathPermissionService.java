package com.simplejourney.security.services;

import com.simplejourney.security.entities.Permission;

import java.util.List;

public interface PathPermissionService {
    List<Permission.Permissions> findByPathAndMethod(String path, String method);
}
