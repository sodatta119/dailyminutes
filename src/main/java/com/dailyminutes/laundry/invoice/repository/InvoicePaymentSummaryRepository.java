/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.repository; // Updated package name

import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface InvoicePaymentSummaryRepository extends ListCrudRepository<InvoicePaymentSummaryEntity, Long> {
    List<InvoicePaymentSummaryEntity> findByInvoiceId(Long invoiceId);

    Optional<InvoicePaymentSummaryEntity> findByPaymentId(Long paymentId); // Useful for updates from Payment events

    List<InvoicePaymentSummaryEntity> findByStatus(String status);

    List<InvoicePaymentSummaryEntity> findByMethod(String method);
}
