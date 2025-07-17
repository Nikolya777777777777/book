package com.example.demo.service.shoppingcart;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartByUserName(String userName);
    ShoppingCartResponseDto saveShoppingCart(ShoppingCart shoppingCart);
    boolean deleteCartItemInShoppingCart(ShoppingCart shoppingCart, CartItem cartItem);
}
