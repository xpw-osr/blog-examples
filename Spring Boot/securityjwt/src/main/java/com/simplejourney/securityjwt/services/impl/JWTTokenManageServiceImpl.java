package com.simplejourney.securityjwt.services.impl;

import com.simplejourney.securityjwt.services.JWTTokenManageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JWTTokenManageServiceImpl implements JWTTokenManageService {
    private Map<String, Object> tokens = new HashMap<>();

    public void add(String ident, Object token) {
        this.tokens.put(ident, token);
    }

    public void remove(String ident) {
        this.tokens.remove(ident);
    }

    public boolean has(String ident) {
        return this.tokens.containsKey(ident);
    }
}
