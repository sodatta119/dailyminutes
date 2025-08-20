package com.dailyminutes.laundry.order.service;


import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderItemEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.repository.OrderItemRepository;
import com.dailyminutes.laundry.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The type Order query service test.
 */
@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @InjectMocks
    private OrderQueryService orderQueryService;

    /**
     * Find order by id should return order.
     */
    @Test
    void findOrderById_shouldReturnOrder() {
        OrderEntity order = new OrderEntity(1L, 1L, 1L, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.TEN);
        OrderItemEntity orderItem = new OrderItemEntity();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of(orderItem));

        assertThat(orderQueryService.findOrderById(1L)).isPresent();
    }
}
