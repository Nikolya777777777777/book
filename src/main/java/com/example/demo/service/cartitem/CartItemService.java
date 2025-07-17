package com.example.demo.service.cartitem;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.model.CartItem;

import java.util.Set;

public interface CartItemService {
    CartItem checkIfCartItemExistInShoppingCart(Set<CartItem> cartItems, Long id);

    CartItemResponseDto save(CartItem updatedCartItem);
}
