package com.example.demo.service.shoppingcart.impl;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.shoppingcart.ShoppingCartMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCartByUserId(Long id) {
        return shoppingCartMapper.toResponseDto(shoppingCartRepository
                .getShoppingCartByUserId(id));
    }

    @Override
    public ShoppingCartResponseDto addCartItemToShoppingCart(CartItem cartItem,
                                                             ShoppingCart shoppingCart) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);
        return shoppingCartMapper.toResponseDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public boolean deleteCartItemInShoppingCart(ShoppingCart shoppingCart, Long cartItemId) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId().equals(cartItemId)) {
                cartItemRepository.deleteById(cartItem.getId());
                return true;
            }
        }
        return false;
    }
}
