package com.dailyminutes.laundry.invoice.service;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceItemEntity;
import com.dailyminutes.laundry.invoice.repository.InvoiceItemRepository;
import com.dailyminutes.laundry.invoice.repository.InvoiceRepository;
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

@ExtendWith(MockitoExtension.class)
class InvoiceQueryServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceItemRepository invoiceItemRepository;
    @InjectMocks
    private InvoiceQueryService invoiceQueryService;

    @Test
    void findInvoiceById_shouldReturnInvoice() {
        InvoiceEntity invoice = new InvoiceEntity(1L, "swipe1", 1L, LocalDateTime.now(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO);
        InvoiceItemEntity invoiceItem = new InvoiceItemEntity();
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceItemRepository.findByInvoiceId(1L)).thenReturn(List.of(invoiceItem));
        assertThat(invoiceQueryService.findInvoiceById(1L)).isPresent();
    }
}
