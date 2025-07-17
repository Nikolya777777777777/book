package com.example.demo.service.shoppingcart.impl;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.shoppingcart.ShoppingCartMapper;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCartByUserName(String userName) {
        return shoppingCartMapper.toResponseDto(shoppingCartRepository
                .getShoppingCartByUserName(userName));
    }
}
