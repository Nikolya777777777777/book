package com.example.demo.repository.shoppingcart;

import com.example.demo.dto.shoppingcart.ShoppingCartRequestDto;
import com.example.demo.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart getShoppingCartByUserName(String userName);
    ShoppingCart toEntity(ShoppingCartRequestDto shoppingCartRequestDto);
}
