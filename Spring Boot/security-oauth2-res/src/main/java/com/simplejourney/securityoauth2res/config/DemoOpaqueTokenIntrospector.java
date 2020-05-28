package com.simplejourney.securityoauth2res.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * We need replace introsepctor with customized because getScope() method of TokenIntrospectionSuccessResponse cannot deserialize scope of type Set<String>
 * see [TokenIntrospectionSuccessResponse doesn't support parsing scopes presented as JSONArray #7563](https://github.com/spring-projects/spring-security/issues/7563)
 */

public class DemoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private OpaqueTokenIntrospector delegate;

    public DemoOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        this.delegate = new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        return oAuth2AuthenticatedPrincipal(principal);
    }

    private DefaultOAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal(OAuth2AuthenticatedPrincipal principal) {
        /**
         * In ClientDetails interfaces of oauth2 server, there are 2 types of things to indicate the permissions of client
         * 1. scopes
         * 2. authorities
         * You can get both of them from 'principal.attributes' which got from auth server
         * Generally, you can use scope to grant permission for client,  -> 'SCOPE_'
         * And also, you can use 'authorities' to do it.    -> 'ROLE_'
         * Or, both of them
         *
         * But, notice, the scopes are permissions of client, and authorities are user's.
         * See [Spring oauth2 scope vs authorities(roles)](https://stackoverflow.com/questions/32092749/spring-oauth2-scope-vs-authoritiesroles)
         * And [The OAuth 2.0 Authorization Framework - 3.3.  Access Token Scope](https://tools.ietf.org/html/rfc6749#section-3.3)
         */

        List<String> scopes = principal.getAttribute(OAuth2IntrospectionClaimNames.SCOPE);
        Collection<GrantedAuthority> grantedAuthorities = scopes.stream().map(scope -> "SCOPE_" + scope).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

//        List<String> authorities = principal.getAttribute("authorities");
//        Collection<GrantedAuthority> grantedAuthorities = authorities.stream().map(authority -> "ROLE_" + authority).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new DefaultOAuth2AuthenticatedPrincipal(principal.getName(), principal.getAttributes(), grantedAuthorities);
    }
}
