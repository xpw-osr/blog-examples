package com.simplejourney.security.config;

import com.simplejourney.security.entities.Permission;
import com.simplejourney.security.services.PathPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * for Custom Access Decision
 */

@Component
public class DemoFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private PathPermissionService pathPermissionService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        String method = ((FilterInvocation) o).getRequest().getMethod();

        System.out.println(String.format("[DemoFilterInvocationSecurityMetadataSource] Request URL: %s", requestUrl));

        List<Permission.Permissions> permissions = pathPermissionService.findByPathAndMethod(requestUrl, method);
        if (null == permissions || permissions.isEmpty()) {
            return null;
        }

        String[] attributes = new String[permissions.size()];
        for (int index = 0; index < permissions.size(); index++) {
            // #################################################
            // # for simple Permission Enum Definition - BEGIN
            attributes[index] = permissions.get(index).name();
            // # for simple Permission Enum Definition - BEGIN
            // #################################################


            // #################################################
            // # for complex Permission Enum Definition - BEGIN
//            attributes[index] = permissions.get(index).code();
            // # for complex Permission Enum Definition - BEGIN
            // #################################################
        }

        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
