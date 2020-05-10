package com.simplejourney.security.services;

import com.simplejourney.security.entities.User;

public interface DemoUserService {
    User findByName(String name);
}
