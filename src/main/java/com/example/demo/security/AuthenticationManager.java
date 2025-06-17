package com.example.demo.security;

import com.example.demo.dto.user.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManager {
    private final AuthenticationService authenticationService;

    public boolean isAuthenticated(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        String email = authentication.getPrincipal();
        String password = authentication.getCredentials();

        return authenticationService.authenticate(new UserLoginRequestDto(email, password));
    }
}
