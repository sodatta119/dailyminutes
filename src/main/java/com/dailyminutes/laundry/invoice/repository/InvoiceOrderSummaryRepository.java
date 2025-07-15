/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.repository; // Updated package name

import com.dailyminutes.laundry.invoice.domain.model.InvoiceOrderSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceOrderSummaryRepository extends ListCrudRepository<InvoiceOrderSummaryEntity, Long> {
    Optional<InvoiceOrderSummaryEntity> findByInvoiceId(Long invoiceId); // Useful for updates from Invoice events
    Optional<InvoiceOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events
    List<InvoiceOrderSummaryEntity> findByStatus(String status);
}
