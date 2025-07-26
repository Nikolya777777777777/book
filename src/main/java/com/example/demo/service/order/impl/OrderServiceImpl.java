package com.example.demo.service.order.impl;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.order.OrderMapper;
import com.example.demo.mapper.orderitem.OrderItemMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.order.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can "
                        + "not find shoppingCart with user id: " + user.getId()));
        Set<OrderItem> orderItems = new HashSet<>();
        Order order = new Order();
        BigDecimal totalPrice = BigDecimal.ZERO;
        order.setUser(user);
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(totalPrice);
        order.setStatus(Status.PENDING);
        Set<CartItem> cartItems = cartItemRepository
                .getAllCartItemsByShoppingCartId(shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can "
                        + "not find cartItems by shoppingCart id: " + shoppingCart.getId()));
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = orderItemMapper.convertCartItemToOrderItem(cartItem);
            totalPrice = totalPrice.add(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderResponseDto> getByAllOrdersByUserId(Long userId) {
        Set<Order> orders = orderRepository.findAllOrdersByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can "
                        + "not find order by user id: " + userId));
        return orders.stream().map(orderMapper::toOrderResponseDto).collect(Collectors.toSet());
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            UpdateOrderStatusRequestDto updateOrderStatusRequestDto,
                                              Long orderId, User user) {
        Order order = orderRepository.findOrderWithIdByUserId(user.getId(), orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find order with id: "
                        + orderId + " and by user id: " + user.getId()));
        order.setStatus(updateOrderStatusRequestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }
}
