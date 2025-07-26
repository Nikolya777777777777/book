package com.example.demo.controller;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.model.User;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@Tag(name = "Order Controller", description = "Endpoints for managing the Orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                        @AuthenticationPrincipal User user) {
        return orderService.createOrder(orderRequestDto, user);
    }

    @GetMapping
    public Set<OrderResponseDto> getAllUserOrders(@AuthenticationPrincipal User user) {
        return orderService.getByAllOrdersByUserId(user.getId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(@RequestBody @Valid UpdateOrderStatusRequestDto updateOrderStatusRequestDto,
                                              @PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderService.updateOrderStatus(updateOrderStatusRequestDto, id, user);
    }
}
