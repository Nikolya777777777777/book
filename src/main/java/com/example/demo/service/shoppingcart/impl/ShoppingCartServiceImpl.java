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
import com.example.demo.repository.cartitem.CartItemRepository;
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
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCartByUserId(Long id) {
        return shoppingCartMapper.toResponseDto(shoppingCartRepository
                .getShoppingCartByUserId(id).orElseThrow(() -> new EntityNotFoundException("Can "
                        + "not find shoppingCart with user id: " + id)));
    }

    @Override
    public ShoppingCartResponseDto addCartItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                             User user) {
        Book book = bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can not find book with id: "
                        + cartItemRequestDto.getBookId()));
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find "
                        + "shoppingCart with user id: " + user.getId()));
        Optional<CartItem> optionalCartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(),
                        cartItemRequestDto.getBookId());
        if (optionalCartItem.isPresent()) {
            CartItem existingItem = optionalCartItem.get();
            existingItem.setQuantity(cartItemRequestDto.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newCartItem = cartItemMapper.toEntity(cartItemRequestDto);
            newCartItem.setShoppingCart(shoppingCart);
            cartItemRepository.save(newCartItem);
        }
        return shoppingCartMapper.toResponseDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteCartItemInShoppingCart(User user, Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find "
                        + "shoppingCart with user id: " + user.getId()));
        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(id,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find cartItem with id: "
                        + id + " in shoppingCart with user id: " + user.getId()));
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
    public CartItemRequestDto updateCartItemDto(Long cartItemId, Long userId,
                                                UpdateCartItemDto updateCartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find shoppingCart with "
                        + "user id: " + userId));
        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(cartItemId,
                        shoppingCart.getId())
                .map(newCartItem -> {
                    newCartItem.setQuantity(updateCartItemDto.getQuantity());
                    return newCartItem;
                })
                .orElseThrow(() -> new EntityNotFoundException("Can not find cartItem with id: "
                        + cartItemId + " and user id: " + userId));
        return cartItemMapper.toRequestDto(cartItemRepository.save(cartItem));
    }
}
