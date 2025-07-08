package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private static final String SEPARATOR = ":";
    private static final String SECRET = "mySuperSecretString";

    public String generateToken(String username) {
        String rawToken = username + SEPARATOR + System.currentTimeMillis() + SEPARATOR + SECRET;
        return toHexString(rawToken.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isValidToken(String token) {
        try {
            String rawToken = new String(fromHexString(token), StandardCharsets.UTF_8);
            String[] parts = rawToken.split(SEPARATOR);
            if (parts.length == 3) {
                return SECRET.equals(parts[2]);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
        return false;
    }

    public String extractUsername(String token) {
        if (!isValidToken(token)) {
            return null;
        }
        String rawToken = new String(fromHexString(token), StandardCharsets.UTF_8);
        return rawToken.split(SEPARATOR)[0];
    }

    private String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private byte[] fromHexString(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
