/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.service;


import com.dailyminutes.laundry.invoice.dto.*;
import com.dailyminutes.laundry.invoice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceCustomerSummaryRepository customerSummaryRepository;
    private final InvoiceOrderSummaryRepository orderSummaryRepository;
    private final InvoicePaymentSummaryRepository paymentSummaryRepository;

    public Optional<InvoiceResponse> findInvoiceById(Long id) {
        return invoiceRepository.findById(id).map(invoice -> {
            List<InvoiceItemDto> items = invoiceItemRepository.findByInvoiceId(id).stream()
                    .map(item -> new InvoiceItemDto(item.getId(), item.getCatalogId(), item.getQuantity(), item.getItemPrice(), item.getTax()))
                    .collect(Collectors.toList());
            return new InvoiceResponse(invoice.getId(), invoice.getSwipeInvoiceId(), invoice.getCustomerId(), invoice.getInvoiceDate(), invoice.getTotalPrice(), invoice.getTotalTax(), invoice.getTotalDiscount(), items);
        });
    }

    public List<InvoiceResponse> findAllInvoices() {
        return StreamSupport.stream(invoiceRepository.findAll().spliterator(), false)
                .map(invoice -> findInvoiceById(invoice.getId()).orElse(null))
                .collect(Collectors.toList());
    }

    public List<InvoiceCustomerSummaryResponse> findCustomerSummariesByInvoiceId(Long invoiceId) {
        return customerSummaryRepository.findByInvoiceId(invoiceId).stream()
                .map(s -> new InvoiceCustomerSummaryResponse(s.getId(), s.getInvoiceId(), s.getCustomerId(), s.getCustomerName(), s.getCustomerPhoneNumber(), s.getCustomerEmail()))
                .collect(Collectors.toList());
    }

    public List<InvoiceOrderSummaryResponse> findOrderSummariesByInvoiceId(Long invoiceId) {
        return orderSummaryRepository.findByInvoiceId(invoiceId).stream()
                .map(s -> new InvoiceOrderSummaryResponse(s.getId(), s.getInvoiceId(), s.getOrderId(), s.getOrderDate(), s.getStatus(), s.getTotalAmount()))
                .collect(Collectors.toList());
    }

    public List<InvoicePaymentSummaryResponse> findPaymentSummariesByInvoiceId(Long invoiceId) {
        return paymentSummaryRepository.findByInvoiceId(invoiceId).stream()
                .map(s -> new InvoicePaymentSummaryResponse(s.getId(), s.getInvoiceId(), s.getPaymentId(), s.getPaymentDateTime(), s.getAmount(), s.getStatus(), s.getMethod(), s.getTransactionId()))
                .collect(Collectors.toList());
    }
}
