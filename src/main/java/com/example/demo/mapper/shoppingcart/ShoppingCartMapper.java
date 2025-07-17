package com.example.demo.mapper.shoppingcart;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);

}
