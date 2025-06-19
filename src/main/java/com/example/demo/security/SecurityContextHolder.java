package com.example.demo.security;

public class SecurityContextHolder {
    private static ThreadLocal<SecurityContext> securityContext = new ThreadLocal<>() {
        @Override
        protected SecurityContext initialValue() {
            return new SecurityContext();
        }
    };

    public static SecurityContext getSecurityContext() {
        return securityContext.get();
    }
}
