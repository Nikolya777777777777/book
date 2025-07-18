package com.example.demo.controller;

import com.example.demo.dto.shoppingcart.ShoppingCartRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartRequestDtoWithoutBookId;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.cartitem.CartItemMapper;
import com.example.demo.mapper.shoppingcart.ShoppingCartMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.service.cartitem.CartItemService;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;

    @GetMapping
    public ShoppingCartResponseDto getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = authentication.getName();
        return shoppingCartService.getShoppingCartByUserEmail(username);
    }

    @PostMapping
    public ShoppingCartResponseDto createShoppingCart(
            @RequestBody ShoppingCartRequestDto shoppingCartRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = cartItemMapper.shopingCartRequestDtoToCartItem(shoppingCartRequestDto);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(Set.of(cartItem));
        shoppingCart.setUser(user);
        cartItem.setShoppingCart(shoppingCart);
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartService
                .saveShoppingCart(shoppingCart);
        return shoppingCartResponseDto;
    }

    @PutMapping("/items{id}")
    public ShoppingCartResponseDto updateShoppingCart(@PathVariable Long id,
                                                      @RequestBody
                                                      ShoppingCartRequestDtoWithoutBookId
                                                              shoppingCartRequestDtoWithoutBookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ShoppingCart shoppingCart = shoppingCartMapper.toEntityFromResponseDto(shoppingCartService
                .getShoppingCartByUserEmail(username));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        CartItem cartItemFromDb = cartItemService.checkIfCartItemExistInShoppingCart(cartItems, id);
        CartItem updatedCartItem = cartItemMapper.updateCartItemFromDb(cartItemFromDb,
                shoppingCartRequestDtoWithoutBookId);
        cartItemService.save(updatedCartItem);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @DeleteMapping("items{id}")
    public void deleteCartItemInShoppingCart(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ShoppingCart shoppingCart = shoppingCartMapper.toEntityFromResponseDto(shoppingCartService
                .getShoppingCartByUserEmail(username));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        CartItem cartItem = cartItemService.checkIfCartItemExistInShoppingCart(cartItems, id);
        shoppingCartService.deleteCartItemInShoppingCart(shoppingCart, cartItem);
    }
}
