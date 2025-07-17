package com.example.demo.service.cartitem.impl;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.mapper.cartitem.CartItemMapper;
import com.example.demo.model.CartItem;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.cartitem.CartItemService;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItem checkIfCartItemExistInShoppingCart(Set<CartItem> cartItems, Long id) {
        for (CartItem cartItem : cartItems) {
            if (Objects.equals(cartItem.getId(), id)) {
                return cartItem;
            }
        }
        throw new RuntimeException("CartItem with id " + id + " was not found");
    }

    @Override
    public CartItemResponseDto save(CartItem updatedCartItem) {
        return cartItemMapper.toResponseDto(cartItemRepository.save(updatedCartItem));
    }
}
