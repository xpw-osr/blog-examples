package com.simplejourney.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * For login with Json data in body
 */

@Component
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * Must has, otherwise following error will be occurred during launching application.
     * "java.lang.IllegalArgumentException: authenticationManager must be specified"
     */
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
            try (InputStream is = request.getInputStream()) {
                Map<String, String> authentication = mapper.readValue(is, Map.class);
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.get("username"), authentication.get("password"));
            } catch (Exception ex) {
                ex.printStackTrace();
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("", "");
            } finally {
                setDetails(request, usernamePasswordAuthenticationToken);
                return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
            }
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}
