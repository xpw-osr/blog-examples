package com.simplejourney.securityoauth2auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

public class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter {
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        return super.convertUserAuthentication(authentication);

        /**
         * You can do some customization here as following example code
         */
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("sub", authentication.getName());
//        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
//            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
//        }
//        return response;
    }
}
