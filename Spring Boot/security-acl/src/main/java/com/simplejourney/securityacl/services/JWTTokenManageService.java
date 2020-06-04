package com.simplejourney.securityacl.services;

public interface JWTTokenManageService {
    void add(String ident, Object token);
    void remove(String ident);
    boolean has(String ident);
}

