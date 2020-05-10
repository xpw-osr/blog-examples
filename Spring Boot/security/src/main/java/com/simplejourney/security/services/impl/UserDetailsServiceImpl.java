package com.simplejourney.security.services.impl;

import com.simplejourney.security.entities.Permission;
import com.simplejourney.security.entities.Role;
import com.simplejourney.security.entities.User;
import com.simplejourney.security.services.DemoUserService;
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
                // #################################################
                // # for simple Permission Enum Definition - BEGIN
                authorities.add(new SimpleGrantedAuthority(permission.getPermission().name()));
                // # for simple Permission Enum Definition - END
                // #################################################

                // #################################################
                // # for complex Permission Enum Definition - BEGIN
                //authorities.add(new SimpleGrantedAuthority(permission.getPermission().code()));
                // # for complex Permission Enum Definition - END
                // #################################################
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }
}
