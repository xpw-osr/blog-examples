package com.simplejourney.securityoauth2auth.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Value("${use_jwt_token}")
    private boolean useJwtToken;

    @Autowired
    private TokenStore tokenStore;

    @PostMapping("/info")
    public ResponseEntity<Principal> info(String access_token) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(access_token);
        if (null == authentication) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username;
        if (this.useJwtToken) {
            username = authentication.getUserAuthentication().getPrincipal().toString();
        } else {
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            username = user.getUsername();
        }

        return ResponseEntity.ok(new UserPrincipal(username));
    }

    @Data
    @AllArgsConstructor
    public class UserPrincipal implements Principal {
        private String name;

        @Override
        public String getName() {
            return name;
        }
    }
}
