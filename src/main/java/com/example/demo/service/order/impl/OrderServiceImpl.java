package com.example.demo.service.order.impl;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
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
import com.example.demo.repository.orderitem.OrderItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.order.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        Order order = new Order();
        OrderItem orderItemToSave = new OrderItem();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can "
                + "not find shoppingCart with user id: " + user.getId()));
        Set<CartItem> cartItems = cartItemRepository
                .getAllCartItemsByShoppingCartId(shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find "
                        + "cartItems by shoppingCart id: " + shoppingCart.getId()));
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            orderItemToSave = orderItemMapper.convertCartItemToOrderItem(cartItem);
            orderItemToSave.setOrder(order);
            orderItemToSave.setBook(cartItem.getBook());
            totalPrice = totalPrice.add(cartItem.getBook().getPrice());
            orderItemRepository.save(orderItemToSave);
            orderItems.add(orderItemToSave);
        }
        order.setOrderItems(orderItems);
        order.setUser(user);
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(totalPrice);
        order.setStatus(Status.PENDING);
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }
}
