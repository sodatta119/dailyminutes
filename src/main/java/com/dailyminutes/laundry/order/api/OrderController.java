/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.api;

import com.dailyminutes.laundry.order.dto.*;
import com.dailyminutes.laundry.order.service.OrderQueryService;
import com.dailyminutes.laundry.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody UpdateOrderRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(orderService.updateOrder(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return orderQueryService.findOrderById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderQueryService.findAllOrders());
    }

    @GetMapping("/{id}/customer-summary")
    public ResponseEntity<List<OrderCustomerSummaryResponse>> getCustomerSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderQueryService.findCustomerSummaryByOrderId(id));
    }

    @GetMapping("/{id}/store-summary")
    public ResponseEntity<List<OrderStoreSummaryResponse>> getStoreSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderQueryService.findStoreSummaryByOrderId(id));
    }

    @GetMapping("/{id}/task-summary")
    public ResponseEntity<List<OrderTaskSummaryResponse>> getTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderQueryService.findTaskSummaryByOrderId(id));
    }

    @GetMapping("/{id}/payment-summary")
    public ResponseEntity<List<OrderPaymentSummaryResponse>> getPaymentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderQueryService.findPaymentSummaryByOrderId(id));
    }

    @GetMapping("/{id}/invoice-summary")
    public ResponseEntity<List<OrderInvoiceSummaryResponse>> getInvoiceSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderQueryService.findInvoiceSummaryByOrderId(id));
    }
}
