package com.example.demo.mapper.orderitem;

import com.example.demo.config.MapperConfig;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.price", target = "price")
    OrderItem convertCartItemToOrderItem(CartItem cartItem);
}
