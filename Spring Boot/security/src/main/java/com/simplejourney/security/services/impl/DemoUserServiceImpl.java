package com.simplejourney.security.services.impl;

import com.simplejourney.security.entities.Permission;
import com.simplejourney.security.entities.Role;
import com.simplejourney.security.entities.User;
import com.simplejourney.security.services.DemoUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class DemoUserServiceImpl implements DemoUserService {
    private Map<String, User> users = new HashMap<String, User>() {{
        put("jerry", new User(0, "jerry", "$2a$10$slYQmyNdGzTn7ZLBXBChFOCHQhUkTikWVg2V95lHK7HRj/LPjaZIa",
                new ArrayList<Role>() {{
                    add(new Role(0, Role.Roles.Admin, new ArrayList<Permission>() {{
                        add(new Permission(0, Permission.Permissions.Create));
                        add(new Permission(0, Permission.Permissions.Delete));
                        add(new Permission(0, Permission.Permissions.Modify));
                        add(new Permission(0, Permission.Permissions.View));
                    }}));
                }}));

        put("tom", new User(0, "tom", "$2a$10$slYQmyNdGzTn7ZLBXBChFOCHQhUkTikWVg2V95lHK7HRj/LPjaZIa",
                new ArrayList<Role>() {{
                    add(new Role(0, Role.Roles.Vistor, new ArrayList<Permission>() {{
                        add(new Permission(0, Permission.Permissions.View));
                    }}));
                }}));
    }};

    public User findByName(String name) {
        return this.users.get(name);
    }
}
