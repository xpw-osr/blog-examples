package com.simplejourney.securityoauth2auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class DemoTokenEnhancer implements TokenEnhancer {
    @Value("${use_jwt_token}")
    private boolean useJwtToken;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        System.out.println(String.format("Token: %s", oAuth2AccessToken));
        System.out.println(String.format("Authentication: %s", oAuth2Authentication));

        /**
         * If you want to add more additional information to token, you can do it here like following example
         */
//        User user = (User) oAuth2Authentication.getPrincipal();
//        final Map<String, Object> additionalInfo = new HashMap<>();
//        additionalInfo.put("user_name", user.getUsername());
//        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);

        return oAuth2AccessToken;
    }
}
