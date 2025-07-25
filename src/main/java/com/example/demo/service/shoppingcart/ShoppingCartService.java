package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCartByUserId(Long id);

    ShoppingCartResponseDto addCartItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                      User user);

    void deleteCartItemInShoppingCart(User user, Long id);

    void createShoppingCartForUser(User user);

    CartItemRequestDto updateCartItemDto(Long cartItemId, Long userId,
                                         UpdateCartItemDto updateCartItemDto);
}
