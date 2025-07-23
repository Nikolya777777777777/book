package com.example.demo.controller;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Endpoints for managing the shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get current user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Shopping cart retrieved successfully")
    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCartByUserId(user.getId());
    }

    @Operation(summary = "Create a new shopping cart with one item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping cart created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ShoppingCartResponseDto createCartItemShoppingCart(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.addCartItemToShoppingCart(cartItemRequestDto, user);
    }

    @Operation(summary = "Update item in the shopping cart by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/items/{id}")
    public ShoppingCartResponseDto updateCartItemInShoppingCart(@PathVariable Long id,
                                                      @RequestBody @Valid
                                                      UpdateCartItemDto
                                                              updateCartItemDto,
                                                      @AuthenticationPrincipal User user) {
        return shoppingCartService.addCartItemToShoppingCart(shoppingCartService
                .updateCartItemDto(id, user.getId(), updateCartItemDto), user);
    }

    @Operation(summary = "Delete cart item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("items/{id}")
    public void deleteCartItemInShoppingCart(@PathVariable Long id,
                                             @AuthenticationPrincipal User user) {
        shoppingCartService.deleteCartItemInShoppingCart(user, id);
    }
}
