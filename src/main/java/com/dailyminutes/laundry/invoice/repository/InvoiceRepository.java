/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.invoice.repository;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Invoice repository.
 */
public interface InvoiceRepository extends ListCrudRepository<InvoiceEntity, Long> {
    /**
     * Find by swipe invoice id optional.
     *
     * @param swipeInvoiceId the swipe invoice id
     * @return the optional
     */
    Optional<InvoiceEntity> findBySwipeInvoiceId(String swipeInvoiceId);

    /**
     * Find all by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<InvoiceEntity> findAllByCustomerId(Long customerId);

    /**
     * Find all by order id list.
     *
     * @param orderId the customer id
     * @return the list
     */
    List<InvoiceEntity> findAllByOrderId(Long orderId);

    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<InvoiceEntity> findByOrderId(Long orderId);
}

