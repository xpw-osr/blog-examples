package com.simplejourney.securityoauth2auth.services.impl;

import com.simplejourney.securityoauth2auth.entities.Permission;
import com.simplejourney.securityoauth2auth.entities.Role;
import com.simplejourney.securityoauth2auth.entities.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DemoUserDetailsServiceImpl implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = users.get(s);

        if (null == user) {
            return null;
        }

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getPermission().name()));
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }
}
