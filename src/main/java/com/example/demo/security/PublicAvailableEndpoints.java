package com.example.demo.security;

import java.util.List;

public class PublicAvailableEndpoints {
    protected static List<String> publicEndPoints = List.of(
            "/api/auth/registration"
    );

    public static List<String> getPublicEndpoints() {
        return publicEndPoints;
    }
}
