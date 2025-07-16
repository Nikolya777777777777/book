package com.example.demo.service.shoppingCart.impl;

import com.example.demo.dto.shoppingCart.ShoppingCartResponseDto;
import com.example.demo.repository.shoppingCart.ShoppingCartRepository;
import com.example.demo.service.shoppingCart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        //return shoppingCartRepository.findById(Long.valueOf("100"));
        return null;
    }
}
