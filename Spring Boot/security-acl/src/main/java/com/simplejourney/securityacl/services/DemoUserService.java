package com.simplejourney.securityacl.services;

import com.simplejourney.securityacl.entities.User;

public interface DemoUserService {
    User findByName(String name);
}
