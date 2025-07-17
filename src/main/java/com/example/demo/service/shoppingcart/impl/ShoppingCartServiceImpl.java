package com.example.demo.service.shoppingcart.impl;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
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
