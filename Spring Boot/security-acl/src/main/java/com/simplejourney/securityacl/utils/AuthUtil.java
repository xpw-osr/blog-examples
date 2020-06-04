package com.simplejourney.securityacl.utils;

import com.simplejourney.securityacl.entities.User;
import com.simplejourney.securityacl.repositories.UserRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class AuthUtil {
    @Autowired
    private UserRepositoy userRepositoy;

    public User getUserEntity() throws HttpClientErrorException {
        if (null == SecurityContextHolder.getContext()) {
            throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", null, null, null);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (null == auth || !auth.isAuthenticated()) {
            throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", null, null, null);
        }

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        com.simplejourney.securityacl.entities.User userEntity = userRepositoy.getUserByName(user.getUsername());
        if (null == userEntity) {
            throw HttpClientErrorException.create(HttpStatus.NOT_FOUND, "User Not Found", null, null, null);
        }

        return userEntity;
    }
}
