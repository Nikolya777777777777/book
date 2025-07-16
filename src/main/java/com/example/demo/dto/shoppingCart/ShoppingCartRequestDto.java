package com.example.demo.dto.shoppingCart;

import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    private Long bookId;
    private int quantity;
}
