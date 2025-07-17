package com.example.demo.controller;

import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartResponseDto getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return shoppingCartService.getShoppingCart();
    }
}
