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

/**
 * The type Invoice query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceCustomerSummaryRepository customerSummaryRepository;
    private final InvoicePaymentSummaryRepository paymentSummaryRepository;

    /**
     * Find invoice by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<InvoiceResponse> findInvoiceById(Long id) {
        return invoiceRepository.findById(id).map(invoice -> {
            List<InvoiceItemDto> items = invoiceItemRepository.findByInvoiceId(id).stream()
                    .map(item -> new InvoiceItemDto(item.getId(), item.getCatalogId(), item.getQuantity(), item.getItemPrice(), item.getTax()))
                    .collect(Collectors.toList());
            return new InvoiceResponse(invoice.getId(), invoice.getSwipeInvoiceId(), invoice.getOrderId(), invoice.getInvoiceDate(), invoice.getTotalPrice(), invoice.getTotalTax(), invoice.getTotalDiscount(), items);
        });
    }

    /**
     * Find all invoices list.
     *
     * @return the list
     */
    public List<InvoiceResponse> findAllInvoices() {
        return StreamSupport.stream(invoiceRepository.findAll().spliterator(), false)
                .map(invoice -> findInvoiceById(invoice.getId()).orElse(null))
                .collect(Collectors.toList());
    }

    /**
     * Find customer summary by invoice id optional.
     *
     * @param invoiceId the invoice id
     * @return the optional
     */
    public Optional<InvoiceCustomerSummaryResponse> findCustomerSummaryByInvoiceId(Long invoiceId) {
        return customerSummaryRepository.findByInvoiceId(invoiceId)
                .map(s -> new InvoiceCustomerSummaryResponse(s.getId(), s.getInvoiceId(), s.getCustomerId(), s.getCustomerName(), s.getCustomerPhoneNumber(), s.getCustomerEmail()));
    }

    /**
     * Find payment summaries by invoice id list.
     *
     * @param invoiceId the invoice id
     * @return the list
     */
    public List<InvoicePaymentSummaryResponse> findPaymentSummariesByInvoiceId(Long invoiceId) {
        return paymentSummaryRepository.findByInvoiceId(invoiceId).stream()
                .map(s -> new InvoicePaymentSummaryResponse(s.getId(), s.getInvoiceId(), s.getPaymentId(), s.getPaymentDateTime(), s.getAmount(), s.getStatus(), s.getMethod(), s.getTransactionId()))
                .collect(Collectors.toList());
    }
}