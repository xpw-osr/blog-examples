package com.simplejourney.securityoauth2auth.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DemoClientDetails implements ClientDetails {
    private String clientId;
    private Set<String> resId;
    private String clientSecret;
    private Set<String> scope;
    private Set<String> grantTypes;
    private Set<String> redirectUri;
    private Collection<GrantedAuthority> authorities;
    private int accessTokenValiditySeconds;
    private int refreshTokenValiditySeconds;
    private Map<String, Object> additionalInfo;

    public DemoClientDetails(String clientId,
                             Set<String> resId,
                             String clientSecret,
                             Set<String> scope,
                             Set<String> grantTypes,
                             Set<String> uri,
                             Collection<GrantedAuthority> authorities,
                             int accessTokenValiditySeconds,
                             int refreshTokenValiditySeconds,
                             Map<String, Object> additionalInfo) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.resId = resId;
        this.scope = scope;
        this.grantTypes = grantTypes;
        this.redirectUri = uri;
        this.authorities = authorities;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resId;
    }

    @Override
    public boolean isSecretRequired() {
        return false;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return grantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return redirectUri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInfo;
    }
}
