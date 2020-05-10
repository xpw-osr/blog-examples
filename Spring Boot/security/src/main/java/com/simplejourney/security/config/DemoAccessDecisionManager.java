package com.simplejourney.security.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * for Custom Access Decision
 */

@Component
public class DemoAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute ca = iterator.next();
            // permission of current need
            String needRole = ca.getAttribute();
            // all permissions of user's
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                // #################################################
                // # for simple Permission Enum Definition - BEGIN
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
                // # for simple Permission Enum Definition - END
                // #################################################

                // #################################################
                // # for complex Permission Enum Definition - BEGIN
                //Permission.Permissions permission = Permission.Permissions.valueOf(authority.getAuthority());
//                if (permission.code().equals(needRole)) {
//                    return;
//                }
                // # for complex Permission Enum Definition - END
                // #################################################
            }
        }
        throw new AccessDeniedException("No Permission");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
