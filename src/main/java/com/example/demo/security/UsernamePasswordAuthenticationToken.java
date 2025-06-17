package com.example.demo.security;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private String username;

    private String password;

    public UsernamePasswordAuthenticationToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getPrincipal() {
        return username;
    }

    @Override
    public String getCredentials() {
        return password;
    }
}
