package com.simplejourney.securityacl.services.impl;

import com.simplejourney.securityacl.entities.User;
import com.simplejourney.securityacl.repositories.UserRepositoy;
import com.simplejourney.securityacl.services.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoUserServiceImpl implements DemoUserService {
    @Autowired
    private UserRepositoy userRepositoy;

    public User findByName(String name) {
        return userRepositoy.getUserByName(name);
    }
}
