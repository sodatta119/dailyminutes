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

/**
 * The interface Order invoice summary repository.
 */
public interface OrderInvoiceSummaryRepository extends ListCrudRepository<OrderInvoiceSummaryEntity, Long> {
    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<OrderInvoiceSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    /**
     * Find by invoice id optional.
     *
     * @param invoiceId the invoice id
     * @return the optional
     */
    Optional<OrderInvoiceSummaryEntity> findByInvoiceId(Long invoiceId); // Useful for updates from Invoice events

    /**
     * Find by invoice date between list.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the list
     */
    List<OrderInvoiceSummaryEntity> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
