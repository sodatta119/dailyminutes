/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.payment.repository; // Updated package name

import com.dailyminutes.laundry.payment.domain.model.PaymentInvoiceSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentInvoiceSummaryRepository extends ListCrudRepository<PaymentInvoiceSummaryEntity, Long> {
    Optional<PaymentInvoiceSummaryEntity> findByPaymentId(Long paymentId);
    Optional<PaymentInvoiceSummaryEntity> findByInvoiceId(Long invoiceId); // Useful for updates from Invoice events
    List<PaymentInvoiceSummaryEntity> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
