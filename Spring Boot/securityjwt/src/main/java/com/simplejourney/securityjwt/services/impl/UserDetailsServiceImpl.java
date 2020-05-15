package com.simplejourney.securityjwt.services.impl;

import com.simplejourney.securityjwt.entities.Permission;
import com.simplejourney.securityjwt.entities.Role;
import com.simplejourney.securityjwt.entities.User;
import com.simplejourney.securityjwt.services.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private DemoUserService userServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userServiceImpl.findByName(name);
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
