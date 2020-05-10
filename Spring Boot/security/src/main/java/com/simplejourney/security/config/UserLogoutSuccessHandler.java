package com.simplejourney.security.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> results = new HashMap<String, Object>() {{
            put("code", 200);
            put("message", "Logout Succeed");
        }};
        String json = new Gson().toJson(results, new TypeToken<HashMap<String, Object>>(){}.getType());

        httpServletResponse.setContentType("json/application;chartset=utf-8");
        httpServletResponse.getWriter().write(json);
    }
}
