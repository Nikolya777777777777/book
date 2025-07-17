package com.example.demo.mapper.cartitem;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.dto.shoppingcart.ShoppingCartRequestDtoWithoutBookId;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem updateCartItemFromDb(@MappingTarget CartItem cartItem,
                                  ShoppingCartRequestDtoWithoutBookId
                                          shoppingCartRequestDtoWithoutBookId);

    CartItemResponseDto toResponseDto(CartItem cartItem);
}
