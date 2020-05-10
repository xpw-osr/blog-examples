package com.simplejourney.security.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DemoSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        Map<String, Object> results = new HashMap<String, Object>() {{
            put("code", HttpServletResponse.SC_UNAUTHORIZED);
            put("message", "Session Expired");
        }};
        String json = new Gson().toJson(results, new TypeToken<HashMap<String, Object>>(){}.getType());

        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        response.setContentType("json/application;chartset=utf-8");
        response.getWriter().write(json);
    }
}
