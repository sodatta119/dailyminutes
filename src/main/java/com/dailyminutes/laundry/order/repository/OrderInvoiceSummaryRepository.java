/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.order.repository; // Updated package name

import com.dailyminutes.laundry.order.domain.model.OrderInvoiceSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderInvoiceSummaryRepository extends ListCrudRepository<OrderInvoiceSummaryEntity, Long> {
    Optional<OrderInvoiceSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    Optional<OrderInvoiceSummaryEntity> findByInvoiceId(Long invoiceId); // Useful for updates from Invoice events

    List<OrderInvoiceSummaryEntity> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
