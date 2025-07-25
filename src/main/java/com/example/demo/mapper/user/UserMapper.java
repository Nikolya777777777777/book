package com.example.demo.mapper.user;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto modelToResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
