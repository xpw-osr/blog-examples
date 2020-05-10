package com.simplejourney.security.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        Map<String, Object> results = new HashMap<String, Object>() {{
            put("code", 200);
            put("message", "Login Succeed");
        }};
        String json = new Gson().toJson(results, new TypeToken<HashMap<String, Object>>(){}.getType());

        response.setContentType("json/application;chartset=utf-8");
        response.getWriter().write(json);

        // ##################################################################
        // # redirect to AuthController after authentication succeed - BEGIN
        // # ----------------------------------------------------------------
        // # If you want all responses are dueled with Controller, comment above code,
        // # and uncomment following line. It will redirect to AuthController.login
//        this.getRedirectStrategy().sendRedirect(request, response, "/auth/login");
        // # redirect to AuthController after authentication succeed - END
        // ##################################################################
    }
}
