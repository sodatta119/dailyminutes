/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.api;

import com.dailyminutes.laundry.payment.dto.*;
import com.dailyminutes.laundry.payment.service.PaymentQueryService;
import com.dailyminutes.laundry.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentQueryService paymentQueryService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return new ResponseEntity<>(paymentService.createPayment(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @Valid @RequestBody UpdatePaymentRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(paymentService.updatePayment(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return paymentQueryService.findPaymentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentQueryService.findAllPayments());
    }

    @GetMapping("/{id}/customer-summary")
    public ResponseEntity<List<PaymentCustomerSummaryResponse>> getCustomerSummary(@PathVariable Long id) {
        return ResponseEntity.ok(paymentQueryService.findCustomerSummaryByPaymentId(id));
    }

    @GetMapping("/{id}/invoice-summary")
    public ResponseEntity<List<PaymentInvoiceSummaryResponse>> getInvoiceSummary(@PathVariable Long id) {
        return ResponseEntity.ok(paymentQueryService.findInvoiceSummaryByPaymentId(id));
    }

    @GetMapping("/{id}/order-summary")
    public ResponseEntity<List<PaymentOrderSummaryResponse>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.ok(paymentQueryService.findOrderSummaryByPaymentId(id));
    }
}
