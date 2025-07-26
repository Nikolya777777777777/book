package com.example.demo.service.order;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.model.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

public interface OrderService {
    OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, User user);

    Set<OrderResponseDto> getByAllOrdersByUserId(Long userId);

    OrderResponseDto updateOrderStatus(UpdateOrderStatusRequestDto updateOrderStatusRequestDto, Long orderId, User user);
}
