package com.example.demo.service.order;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.model.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, User user);
}
