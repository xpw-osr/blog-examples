package com.simplejourney.security.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class DemoAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Map<String, Object> results = new HashMap<String, Object>() {{
            put("code", HttpServletResponse.SC_UNAUTHORIZED);
            put("message", "Authentication Failed");
            put("error", e.getMessage());
        }};
        String json = new Gson().toJson(results, new TypeToken<HashMap<String, Object>>(){}.getType());

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        httpServletResponse.setContentType("json/application;chartset=utf-8");
        httpServletResponse.getWriter().write(json);
    }
}
