package com.example.demo.security;

import com.example.demo.dto.user.UserLoginRequestDto;
import com.example.demo.model.User;
import com.example.demo.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        return user.isPresent() && user.get().getPassword().equals(requestDto.getPassword());
    }
}
