/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.service;

import com.dailyminutes.laundry.order.dto.*;
import com.dailyminutes.laundry.order.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Order query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderCustomerSummaryRepository customerSummaryRepository;
    private final OrderTaskSummaryRepository taskSummaryRepository;
    private final OrderPaymentSummaryRepository paymentSummaryRepository;
    private final OrderInvoiceSummaryRepository invoiceSummaryRepository;

    /**
     * Find order by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<OrderResponse> findOrderById(Long id) {
        return orderRepository.findById(id).map(order -> {
            List<OrderItemDto> items = orderItemRepository.findByOrderId(id).stream()
                    .map(item -> new OrderItemDto(item.getId(), item.getCatalogId(), item.getQuantity(), item.getItemPriceAtOrder(), item.getNotes()))
                    .collect(Collectors.toList());
            return new OrderResponse(order.getId(), order.getStoreId(), order.getCustomerId(), order.getOrderDate(), order.getStatus(), order.getTotalAmount(), items);
        });
    }

    /**
     * Find all orders list.
     *
     * @return the list
     */
    public List<OrderResponse> findAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(order -> findOrderById(order.getId()).orElse(null))
                .collect(Collectors.toList());
    }

    /**
     * Find customer summary by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    public Optional<OrderCustomerSummaryResponse> findCustomerSummaryByOrderId(Long orderId) {
        return customerSummaryRepository.findByOrderId(orderId)
                .map(s -> new OrderCustomerSummaryResponse(s.getId(), s.getOrderId(), s.getCustomerId(), s.getCustomerName(), s.getCustomerPhoneNumber(), s.getCustomerEmail()));
    }

    /**
     * Find task summary by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    public Optional<OrderTaskSummaryResponse> findTaskSummaryByOrderId(Long orderId) {
        return taskSummaryRepository.findByOrderId(orderId)
                .map(s -> new OrderTaskSummaryResponse(s.getId(), s.getOrderId(), s.getTaskId(), s.getTaskType(), s.getTaskStatus(), s.getTaskStartTime(), s.getAgentId(), s.getAgentName()));
    }

    /**
     * Find payment summary by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    public List<OrderPaymentSummaryResponse> findPaymentSummaryByOrderId(Long orderId) {
        return paymentSummaryRepository.findByOrderId(orderId).stream()
                .map(s -> new OrderPaymentSummaryResponse(s.getId(), s.getOrderId(), s.getPaymentId(), s.getPaymentDateTime(), s.getAmount(), s.getStatus(), s.getMethod(), s.getTransactionId()))
                .collect(Collectors.toList());
    }

    /**
     * Find invoice summary by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    public Optional<OrderInvoiceSummaryResponse> findInvoiceSummaryByOrderId(Long orderId) {
        return invoiceSummaryRepository.findByOrderId(orderId)
                .map(s -> new OrderInvoiceSummaryResponse(s.getId(), s.getOrderId(), s.getInvoiceId(), s.getInvoiceDate(), s.getTotalPrice(), s.getTotalTax(), s.getTotalDiscount()));
    }
}