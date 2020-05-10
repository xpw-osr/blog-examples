package com.simplejourney.security.services.impl;

import com.simplejourney.security.entities.Permission;
import com.simplejourney.security.services.PathPermissionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathPermissionServiceImpl implements PathPermissionService {
    @Data
    @AllArgsConstructor
    class PathPermission {
        private String path;
        private String requestMethod;
        private List<Permission.Permissions> permissions;
    }

    private List<PathPermission> pps = new ArrayList<PathPermission>() {{
        add(new PathPermission("/book", "POST", new ArrayList<Permission.Permissions>() {{ add(Permission.Permissions.Create); }}));
        add(new PathPermission("/book", "DELETE", new ArrayList<Permission.Permissions>() {{ add(Permission.Permissions.Delete); }}));
        add(new PathPermission("/book", "PUT", new ArrayList<Permission.Permissions>() {{ add(Permission.Permissions.Modify); }}));
        add(new PathPermission("/book", "GET", new ArrayList<Permission.Permissions>() {{ add(Permission.Permissions.View); }}));
    }};

    @Override
    public List<Permission.Permissions> findByPathAndMethod(String path, String method) {
        for (PathPermission pp : pps) {
            if (pp.getPath().equals(path) && pp.getRequestMethod().equals(method)) {
                return pp.getPermissions();
            }
        }
        return null;
    }
}
