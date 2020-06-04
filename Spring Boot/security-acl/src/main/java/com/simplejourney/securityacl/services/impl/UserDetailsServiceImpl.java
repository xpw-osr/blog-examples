package com.simplejourney.securityacl.services.impl;

import com.simplejourney.securityacl.entities.User;
import com.simplejourney.securityacl.services.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

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
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }
}
