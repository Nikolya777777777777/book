package com.example.demo.service.shoppingcart.impl;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.cartitem.CartItemMapper;
import com.example.demo.mapper.shoppingcart.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import java.util.Optional;
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
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCartByUserId(Long id) {
        return shoppingCartMapper.toResponseDto(shoppingCartRepository
                .getShoppingCartByUserId(id).orElseThrow(() -> new RuntimeException("Can "
                        + "not find shoppingCart with user id: " + id)));
    }

    @Override
    public ShoppingCartResponseDto addCartItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                             User user) {
        Book book = bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Can not find book with id: "
                        + cartItemRequestDto.getBookId()));
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Can not find shoppingCart with user id: "
                        + user.getId()));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        CartItem cartItem = checkIfBookWithIdExistsInShoppingCart(shoppingCart,
                cartItemRequestDto.getBookId());
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());
        } else {
            CartItem newCartItem = cartItemMapper.toEntity(cartItemRequestDto);
            newCartItem.setShoppingCart(shoppingCart);
            cartItems.add(newCartItem);
        }
        shoppingCart.setCartItems(cartItems);
        return shoppingCartMapper.toResponseDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public CartItem checkIfBookWithIdExistsInShoppingCart(ShoppingCart shoppingCart,
                                                          Long cartItemId) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBook().getId().equals(cartItemId)) {
                return cartItem;
            }
        }
        return null;
    }

    @Override
    public void deleteCartItemInShoppingCart(User user, Long id) {
        ShoppingCart shoppingCart = shoppingCartMapper
                .toEntityFromResponseDto(getShoppingCartByUserId(user.getId()));
        CartItem cartItem = findByCartItemIdAndShoppingCartId(id, shoppingCart.getId());
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.remove(cartItem);
        shoppingCart.setCartItems(cartItems);
    }

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public CartItem findByCartItemIdAndShoppingCartId(Long cartItemId, Long shoppingCartId) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
        Set<CartItem> cartItems = shoppingCart.get().getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId().equals(cartItemId)) {
                return cartItem;
            }
        }
        throw new EntityNotFoundException("CartItem not found with id " + cartItemId
                + " and shoppingCart id " + shoppingCartId);
    }

    @Override
    public CartItemRequestDto updateCartItemDto(UpdateCartItemDto updateCartItemDto, Long id) {
        CartItemRequestDto cartItem = new CartItemRequestDto();
        cartItem.setBookId(id);
        cartItem.setQuantity(updateCartItemDto.getQuantity());
        return cartItem;
    }
}
