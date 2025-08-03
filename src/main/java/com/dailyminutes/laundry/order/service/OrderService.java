/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.service;

import com.dailyminutes.laundry.order.domain.event.*;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderItemEntity;
import com.dailyminutes.laundry.order.dto.CreateOrderRequest;
import com.dailyminutes.laundry.order.dto.OrderItemDto;
import com.dailyminutes.laundry.order.dto.OrderResponse;
import com.dailyminutes.laundry.order.dto.UpdateOrderRequest;
import com.dailyminutes.laundry.order.repository.OrderItemRepository;
import com.dailyminutes.laundry.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ApplicationEventPublisher events;

    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity(null, request.storeId(), request.customerId(), request.orderDate(), request.status(), request.totalAmount());
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderItemEntity> items = request.items().stream()
                .map(itemDto -> new OrderItemEntity(null, savedOrder.getId(), itemDto.catalogId(), itemDto.quantity(), itemDto.itemPriceAtOrder(), itemDto.notes()))
                .collect(Collectors.toList());

        // Use saveAll and collect the saved entities which will now have IDs
        List<OrderItemEntity> savedItems = (List<OrderItemEntity>) orderItemRepository.saveAll(items);

        // Convert the saved items to DTOs for the event
        List<OrderItemInfo> itemInfos = savedItems.stream()
                .map(item -> new OrderItemInfo(item.getId(), item.getCatalogId(), item.getQuantity(), item.getItemPriceAtOrder()))
                .collect(Collectors.toList());

        // Publish the enriched event
        events.publishEvent(new OrderCreatedEvent(savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getStoreId(), savedOrder.getStatus().name(), savedOrder.getTotalAmount(), savedOrder.getOrderDate(), itemInfos));

        return toOrderResponse(savedOrder, savedItems);
    }

    public OrderResponse updateOrder(UpdateOrderRequest request) {
        OrderEntity existingOrder = orderRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Order with ID " + request.id() + " not found."));

        var oldStatus = existingOrder.getStatus();

        existingOrder.setStoreId(request.storeId());
        existingOrder.setCustomerId(request.customerId());
        existingOrder.setOrderDate(request.orderDate());
        existingOrder.setStatus(request.status());
        existingOrder.setTotalAmount(request.totalAmount());
        OrderEntity updatedOrder = orderRepository.save(existingOrder);

        orderItemRepository.deleteAll(orderItemRepository.findByOrderId(updatedOrder.getId()));
        List<OrderItemEntity> items = request.items().stream()
                .map(itemDto -> new OrderItemEntity(null, updatedOrder.getId(), itemDto.catalogId(), itemDto.quantity(), itemDto.itemPriceAtOrder(), itemDto.notes()))
                .collect(Collectors.toList());
        orderItemRepository.saveAll(items);

        events.publishEvent(new OrderUpdatedEvent(
                updatedOrder.getId(),
                updatedOrder.getOrderDate(),
                updatedOrder.getStatus().name(),
                updatedOrder.getTotalAmount()
        ));

        if (oldStatus != updatedOrder.getStatus()) {
            events.publishEvent(new OrderStatusChangedEvent(updatedOrder.getId(), oldStatus, updatedOrder.getStatus()));
        }

        return toOrderResponse(updatedOrder, items);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order with ID " + id + " not found.");
        }
        orderItemRepository.deleteAll(orderItemRepository.findByOrderId(id));
        orderRepository.deleteById(id);
        events.publishEvent(new OrderDeletedEvent(id));
    }

    private OrderResponse toOrderResponse(OrderEntity order, List<OrderItemEntity> items) {
        List<OrderItemDto> itemDtos = items.stream()
                .map(item -> new OrderItemDto(item.getId(), item.getCatalogId(), item.getQuantity(), item.getItemPriceAtOrder(), item.getNotes()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getStoreId(), order.getCustomerId(), order.getOrderDate(), order.getStatus(), order.getTotalAmount(), itemDtos);
    }
}