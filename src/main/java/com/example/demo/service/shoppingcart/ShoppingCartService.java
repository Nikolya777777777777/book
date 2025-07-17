package com.example.demo.service.shoppingcart;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartByUserName(String userName);
}
