package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    @Positive
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}
