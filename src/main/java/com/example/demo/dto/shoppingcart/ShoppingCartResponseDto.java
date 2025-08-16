package com.example.demo.dto.shoppingcart;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}
