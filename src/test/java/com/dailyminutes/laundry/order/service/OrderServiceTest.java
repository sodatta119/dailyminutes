package com.dailyminutes.laundry.order.service;


import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderUpdatedEvent;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.dto.CreateOrderRequest;
import com.dailyminutes.laundry.order.dto.OrderItemDto;
import com.dailyminutes.laundry.order.dto.UpdateOrderRequest;
import com.dailyminutes.laundry.order.repository.OrderItemRepository;
import com.dailyminutes.laundry.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldCreateAndPublishEvent() {
        OrderItemDto item = new OrderItemDto(null, 1L, BigDecimal.ONE, BigDecimal.TEN, "notes");
        CreateOrderRequest request = new CreateOrderRequest(1L, 1L, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.TEN, Collections.singletonList(item));
        OrderEntity order = new OrderEntity(1L, 1L, 1L, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.TEN);
        when(orderRepository.save(any())).thenReturn(order);

        orderService.createOrder(request);

        verify(events).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void updateOrder_shouldUpdateAndPublishEvent() {
        OrderItemDto item = new OrderItemDto(null, 1L, BigDecimal.ONE, BigDecimal.TEN, "notes");
        UpdateOrderRequest request = new UpdateOrderRequest(1L, 1L, 1L, LocalDateTime.now(), OrderStatus.ACCEPTED, BigDecimal.TEN, Collections.singletonList(item));
        OrderEntity order = new OrderEntity(1L, 1L, 1L, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.TEN);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        orderService.updateOrder(request);

        verify(events).publishEvent(any(OrderUpdatedEvent.class));
    }

    @Test
    void deleteOrder_shouldDeleteAndPublishEvent() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(events).publishEvent(any(OrderDeletedEvent.class));
    }

    @Test
    void updateOrder_shouldThrowException_whenOrderNotFound() {
        OrderItemDto item = new OrderItemDto(null, 1L, BigDecimal.ONE, BigDecimal.TEN, "notes");
        UpdateOrderRequest request = new UpdateOrderRequest(1L, 1L, 1L, LocalDateTime.now(), OrderStatus.ACCEPTED, BigDecimal.TEN, Collections.singletonList(item));
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(request));
    }
}
