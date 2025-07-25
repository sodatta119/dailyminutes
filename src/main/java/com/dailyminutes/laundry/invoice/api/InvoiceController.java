/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.api;


import com.dailyminutes.laundry.invoice.dto.*;
import com.dailyminutes.laundry.invoice.service.InvoiceQueryService;
import com.dailyminutes.laundry.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceQueryService invoiceQueryService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return new ResponseEntity<>(invoiceService.createInvoice(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id, @Valid @RequestBody UpdateInvoiceRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(invoiceService.updateInvoice(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return invoiceQueryService.findInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceQueryService.findAllInvoices());
    }

    @GetMapping("/{id}/customer-summary")
    public ResponseEntity<List<InvoiceCustomerSummaryResponse>> getCustomerSummary(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceQueryService.findCustomerSummariesByInvoiceId(id));
    }

    @GetMapping("/{id}/order-summary")
    public ResponseEntity<List<InvoiceOrderSummaryResponse>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceQueryService.findOrderSummariesByInvoiceId(id));
    }

    @GetMapping("/{id}/payment-summary")
    public ResponseEntity<List<InvoicePaymentSummaryResponse>> getPaymentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceQueryService.findPaymentSummariesByInvoiceId(id));
    }
}
