package com.example.demo.service.cartitem.impl;

import com.example.demo.mapper.shoppingcart.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.service.cartitem.CartItemService;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public CartItem createCartItem(Book book, User user) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        ShoppingCart shoppingCart = shoppingCartMapper.toEntityFromResponseDto(shoppingCartService
                .getShoppingCartByUserId(user.getId()));
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }
}
