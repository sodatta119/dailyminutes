package com.dailyminutes.laundry.order.api;

import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.dto.CreateOrderRequest;
import com.dailyminutes.laundry.order.dto.OrderItemDto;
import com.dailyminutes.laundry.order.dto.OrderResponse;
import com.dailyminutes.laundry.order.dto.UpdateOrderRequest;
import com.dailyminutes.laundry.order.service.OrderQueryService;
import com.dailyminutes.laundry.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private OrderQueryService orderQueryService;

    private OrderResponse orderResponse;
    private CreateOrderRequest createOrderRequest;
    private UpdateOrderRequest updateOrderRequest;

    @BeforeEach
    void setUp() {
        OrderItemDto item = new OrderItemDto(1L, 1L, BigDecimal.ONE, BigDecimal.TEN, "notes");
        orderResponse = new OrderResponse(1L, 1L, 1L, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.TEN, Collections.singletonList(item));
        createOrderRequest = new CreateOrderRequest(1L, 1L, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.TEN, Collections.singletonList(item));
        updateOrderRequest = new UpdateOrderRequest(1L, 2L, 2L, LocalDateTime.now(), OrderStatus.ACCEPTED, BigDecimal.ONE, Collections.emptyList());
    }

    @Test
    void createOrder_shouldReturnCreated() throws Exception {
        when(orderService.createOrder(any())).thenReturn(orderResponse);
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {
        when(orderQueryService.findOrderById(1L)).thenReturn(Optional.of(orderResponse));
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(1L));
    }

    @Test
    void updateOrder_shouldReturnOk() throws Exception {
        when(orderService.updateOrder(any())).thenReturn(orderResponse);
        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrder_shouldReturnNoContent() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }
}
