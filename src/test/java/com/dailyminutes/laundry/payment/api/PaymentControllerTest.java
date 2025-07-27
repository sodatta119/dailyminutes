package com.dailyminutes.laundry.payment.api;

import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import com.dailyminutes.laundry.payment.dto.CreatePaymentRequest;
import com.dailyminutes.laundry.payment.dto.PaymentResponse;
import com.dailyminutes.laundry.payment.dto.UpdatePaymentRequest;
import com.dailyminutes.laundry.payment.service.PaymentQueryService;
import com.dailyminutes.laundry.payment.service.PaymentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private PaymentQueryService paymentQueryService;

    private PaymentResponse paymentResponse;
    private CreatePaymentRequest createPaymentRequest;
    private UpdatePaymentRequest updatePaymentRequest;

    @BeforeEach
    void setUp() {
        paymentResponse = new PaymentResponse(1L, 1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        createPaymentRequest = new CreatePaymentRequest(1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        updatePaymentRequest = new UpdatePaymentRequest(1L, 2L, 2L, "txn2", BigDecimal.ONE, LocalDateTime.now(), PaymentStatus.REFUNDED, PaymentMethod.UPI, "refund");
    }

    @Test
    void createPayment_shouldReturnCreated() throws Exception {
        when(paymentService.createPayment(any())).thenReturn(paymentResponse);
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPaymentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getPaymentById_shouldReturnPayment() throws Exception {
        when(paymentQueryService.findPaymentById(1L)).thenReturn(Optional.of(paymentResponse));
        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("txn1"));
    }

    @Test
    void updatePayment_shouldReturnOk() throws Exception {
        when(paymentService.updatePayment(any())).thenReturn(paymentResponse);
        mockMvc.perform(put("/api/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePaymentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deletePayment_shouldReturnNoContent() throws Exception {
        doNothing().when(paymentService).deletePayment(1L);
        mockMvc.perform(delete("/api/payments/1"))
                .andExpect(status().isNoContent());
    }
}