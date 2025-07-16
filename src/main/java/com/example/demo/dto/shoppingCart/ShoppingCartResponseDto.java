package com.example.demo.dto.shoppingCart;

import com.example.demo.dto.cartItem.CartItemResponseDto;
import lombok.Data;
import java.util.List;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long bookId;
    private List<CartItemResponseDto> cartItems;
}
