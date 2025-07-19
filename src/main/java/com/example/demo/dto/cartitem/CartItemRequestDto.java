package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotBlank
    private Long bookId;
    @Positive
    private int quantity;
}
