package com.example.demo.mapper.shoppingcart;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.cartitem.CartItemMapper;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartResponseDto shoppingCartResponseDto,
                           ShoppingCart shoppingCart) {
        if (shoppingCart.getId() != null) {
            shoppingCartResponseDto.setUserId(shoppingCart.getUser().getId());
        }
    }
}
