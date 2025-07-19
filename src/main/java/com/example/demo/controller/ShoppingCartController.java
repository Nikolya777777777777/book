package com.example.demo.controller;

import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.book.BookMapper;
import com.example.demo.mapper.cartitem.CartItemMapper;
import com.example.demo.mapper.shoppingcart.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.service.book.BookService;
import com.example.demo.service.cartitem.CartItemService;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Endpoints for managing the shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;
    private final BookMapper bookMapper;

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
    @PostMapping
    public ShoppingCartResponseDto createCartItemShoppingCart(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            @AuthenticationPrincipal User user) {
        bookService.checkIfBookExists(cartItemRequestDto.getBookId());
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        ShoppingCart shoppingCart = shoppingCartMapper.toEntityFromResponseDto(shoppingCartService
                .getShoppingCartByUserId(user.getId()));
        return shoppingCartService.addCartItemToShoppingCart(cartItem, shoppingCart);
    }

    @Operation(summary = "Update item in the shopping cart by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/items{id}")
    public ShoppingCartResponseDto updateCartItemInShoppingCart(@PathVariable Long id,
                                                      @RequestBody
                                                      UpdateCartItemDto
                                                              updateCartItemDto,
                                                      @AuthenticationPrincipal User user) {
        Book book = bookMapper.dtoToModel(bookService.getBookById(id));
        CartItem cartItem = cartItemService.createCartItem(book, user);
        return shoppingCartService.addCartItemToShoppingCart(cartItem,
                shoppingCartMapper.toEntityFromResponseDto(shoppingCartService
                        .getShoppingCartByUserId(user.getId())));
    }

    @Operation(summary = "Delete cart item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("items{id}")
    public void deleteCartItemInShoppingCart(@PathVariable Long id,
                                             @AuthenticationPrincipal User user) {
        shoppingCartService.deleteCartItemInShoppingCart(shoppingCartMapper
                .toEntityFromResponseDto(shoppingCartService
                        .getShoppingCartByUserId(user.getId())), id);

    }
}
