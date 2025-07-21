package com.example.demo.service.user.impl;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.user.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.enums.RoleName;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import com.example.demo.service.user.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with this email: "
                    + requestDto.getEmail() + " already exist");
        }
        User userToSave = userMapper.toModel(requestDto);
        userToSave.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(RoleName.ROLE_USER + " was not found"));

        userToSave.setRoles(Set.of(userRole));
        userRepository.save(userToSave);
        shoppingCartService.createShoppingCartForUser(userToSave);
        return userMapper.modelToResponse(userToSave);
    }
}
