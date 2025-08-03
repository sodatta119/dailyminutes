package com.dailyminutes.laundry.invoice.service;

import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceUpdatedEvent;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.dailyminutes.laundry.invoice.dto.CreateInvoiceRequest;
import com.dailyminutes.laundry.invoice.dto.InvoiceItemDto;
import com.dailyminutes.laundry.invoice.dto.UpdateInvoiceRequest;
import com.dailyminutes.laundry.invoice.repository.InvoiceItemRepository;
import com.dailyminutes.laundry.invoice.repository.InvoiceRepository;
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
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceItemRepository invoiceItemRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void createInvoice_shouldCreateAndPublishEvent() {
        InvoiceItemDto item = new InvoiceItemDto(null, 1L, 1, BigDecimal.TEN, BigDecimal.ONE);
        CreateInvoiceRequest request = new CreateInvoiceRequest("swipe1", 2L, 1L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, Collections.singletonList(item));
        InvoiceEntity invoice = new InvoiceEntity(1L, "swipe1", 2L, 1L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO);
        when(invoiceRepository.save(any())).thenReturn(invoice);

        invoiceService.createInvoice(request);

        verify(events).publishEvent(any(InvoiceCreatedEvent.class));
    }

    @Test
    void updateInvoice_shouldUpdateAndPublishEvent() {
        InvoiceItemDto item = new InvoiceItemDto(null, 1L, 1, BigDecimal.TEN, BigDecimal.ONE);
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(1L, "swipe1", 1L, 2L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, Collections.singletonList(item));
        InvoiceEntity invoice = new InvoiceEntity(1L, "swipe1", 2L, 1L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any())).thenReturn(invoice);

        invoiceService.updateInvoice(request);

        verify(events).publishEvent(any(InvoiceUpdatedEvent.class));
    }

    @Test
    void deleteInvoice_shouldDeleteAndPublishEvent() {
        when(invoiceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(invoiceRepository).deleteById(1L);

        invoiceService.deleteInvoice(1L);

        verify(events).publishEvent(any(InvoiceDeletedEvent.class));
    }

    @Test
    void updateInvoice_shouldThrowException_whenInvoiceNotFound() {
        InvoiceItemDto item = new InvoiceItemDto(null, 1L, 1, BigDecimal.TEN, BigDecimal.ONE);
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(1L, "swipe1", 1L, 2L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, Collections.singletonList(item));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(request));
    }
}