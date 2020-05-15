package com.simplejourney.securityjwt.services;

import com.simplejourney.securityjwt.entities.User;

public interface DemoUserService {
    User findByName(String name);
}
