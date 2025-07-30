package com.dailyminutes.laundry.invoice.api;

import com.dailyminutes.laundry.invoice.dto.CreateInvoiceRequest;
import com.dailyminutes.laundry.invoice.dto.InvoiceItemDto;
import com.dailyminutes.laundry.invoice.dto.InvoiceResponse;
import com.dailyminutes.laundry.invoice.dto.UpdateInvoiceRequest;
import com.dailyminutes.laundry.invoice.service.InvoiceQueryService;
import com.dailyminutes.laundry.invoice.service.InvoiceService;
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
@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private InvoiceService invoiceService;
    @MockitoBean
    private InvoiceQueryService invoiceQueryService;

    private InvoiceResponse invoiceResponse;
    private CreateInvoiceRequest createInvoiceRequest;
    private UpdateInvoiceRequest updateInvoiceRequest;

    @BeforeEach
    void setUp() {
        InvoiceItemDto item = new InvoiceItemDto(1L, 1L, 1, BigDecimal.TEN, BigDecimal.ONE);
        invoiceResponse = new InvoiceResponse(1L, "swipe1", 1L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, Collections.singletonList(item));
        createInvoiceRequest = new CreateInvoiceRequest("swipe1", 1L, 2L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, Collections.singletonList(item));
        updateInvoiceRequest = new UpdateInvoiceRequest(1L, "swipe2", 2L, 1L, LocalDateTime.now(), BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, Collections.emptyList());
    }

    @Test
    void createInvoice_shouldReturnCreated() throws Exception {
        when(invoiceService.createInvoice(any())).thenReturn(invoiceResponse);
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInvoiceRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getInvoiceById_shouldReturnInvoice() throws Exception {
        when(invoiceQueryService.findInvoiceById(1L)).thenReturn(Optional.of(invoiceResponse));
        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swipeInvoiceId").value("swipe1"));
    }

    @Test
    void updateInvoice_shouldReturnOk() throws Exception {
        when(invoiceService.updateInvoice(any())).thenReturn(invoiceResponse);
        mockMvc.perform(put("/api/invoices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInvoiceRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteInvoice_shouldReturnNoContent() throws Exception {
        doNothing().when(invoiceService).deleteInvoice(1L);
        mockMvc.perform(delete("/api/invoices/1"))
                .andExpect(status().isNoContent());
    }
}