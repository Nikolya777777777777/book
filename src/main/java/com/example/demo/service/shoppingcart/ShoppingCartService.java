package com.example.demo.service.shoppingcart;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCartByUserId(Long id);

    ShoppingCartResponseDto addCartItemToShoppingCart(CartItem cartItem, ShoppingCart shoppingCart);

    boolean deleteCartItemInShoppingCart(ShoppingCart shoppingCart, Long cartItemId);

    void createShoppingCartForUser(User user);
}
