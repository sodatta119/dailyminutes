/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.api;

import com.dailyminutes.laundry.order.dto.*;
import com.dailyminutes.laundry.order.service.OrderQueryService;
import com.dailyminutes.laundry.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody UpdateOrderRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(orderService.updateOrder(request));
    }

    @Operation(summary = "Delete an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get an order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return orderQueryService.findOrderById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @Operation(summary = "Get all orders")
    @ApiResponse(responseCode = "200", description = "List of all orders")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderQueryService.findAllOrders());
    }

    @Operation(summary = "Get customer summary for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found customer summary"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}/customer-summary")
    public ResponseEntity<OrderCustomerSummaryResponse> getCustomerSummary(@PathVariable Long id) {
        return orderQueryService.findCustomerSummaryByOrderId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer summary not found for order"));
    }

    @Operation(summary = "Get store summary for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found store summary"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}/store-summary")
    public ResponseEntity<OrderStoreSummaryResponse> getStoreSummary(@PathVariable Long id) {
        return orderQueryService.findStoreSummaryByOrderId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store summary not found for order"));
    }

    @Operation(summary = "Get task summary for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found task summary"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}/task-summary")
    public ResponseEntity<OrderTaskSummaryResponse> getTaskSummary(@PathVariable Long id) {
        return orderQueryService.findTaskSummaryByOrderId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task summary not found for order"));
    }

    @Operation(summary = "Get payment summary for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found payment summary"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}/payment-summary")
    public ResponseEntity<List<OrderPaymentSummaryResponse>> getPaymentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderQueryService.findPaymentSummaryByOrderId(id));
    }

    @Operation(summary = "Get invoice summary for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found invoice summary"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}/invoice-summary")
    public ResponseEntity<OrderInvoiceSummaryResponse> getInvoiceSummary(@PathVariable Long id) {
        return orderQueryService.findInvoiceSummaryByOrderId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice summary not found for order"));
    }
}