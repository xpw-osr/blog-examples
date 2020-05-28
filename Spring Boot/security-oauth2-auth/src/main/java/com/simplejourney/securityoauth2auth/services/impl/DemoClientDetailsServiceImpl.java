package com.simplejourney.securityoauth2auth.services.impl;

import com.simplejourney.securityoauth2auth.entities.DemoClientDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DemoClientDetailsServiceImpl implements ClientDetailsService {
    private Map<String, DemoClientDetails> clientDetailsMap = new HashMap<String, DemoClientDetails>() {{
        put("client",
                new DemoClientDetails(
                        "client",
                        null,
                        new BCryptPasswordEncoder().encode("secret"),
                        new HashSet<String>() {{ add("APP"); }},
                        new HashSet<String>() {{
                            add("authorization_code");
                            add("refresh_token");  // MUST add this if you need refresh token
                        }},
                        new HashSet<String>() {{ add("http://oauthcli:8080/login/oauth2/code/oauthsvr"); }},
                        new HashSet<GrantedAuthority>() {{ add( new SimpleGrantedAuthority("READ")); }}, // MUST NOT be null
                        (int) TimeUnit.SECONDS.toSeconds(30),
                        (int) TimeUnit.DAYS.toSeconds(15),
                        null));
    }};

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientDetailsMap.get(clientId);
    }
}
