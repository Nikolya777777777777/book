package com.example.demo.mapper.order;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toOrderResponseDto(Order order);
}
