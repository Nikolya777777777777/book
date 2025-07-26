package com.example.demo.service.order;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemsResponseDto;
import com.example.demo.model.User;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, User user);

    Set<OrderResponseDto> getByAllOrdersByUserId(Long userId);

    OrderResponseDto updateOrderStatus(UpdateOrderStatusRequestDto updateOrderStatusRequestDto,
                                       Long orderId, User user);

    Set<OrderItemsResponseDto> getAllOrderItemsInOrder(Long orderId, Long userId);

    OrderItemsResponseDto getOrderItemInOrder(Long orderId, Long orderItemId, Long userId);
}
