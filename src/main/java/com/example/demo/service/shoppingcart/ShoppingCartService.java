package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCartByUserId(Long id);

    ShoppingCartResponseDto addCartItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                      User user);

    CartItem checkIfBookWithIdExistsInShoppingCart(ShoppingCart shoppingCart, Long cartItemId);

    void deleteCartItemInShoppingCart(User user, Long id);

    void createShoppingCartForUser(User user);

    CartItem findByCartItemIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    CartItemRequestDto updateCartItemDto(UpdateCartItemDto updateCartItemDto, Long id);
}
