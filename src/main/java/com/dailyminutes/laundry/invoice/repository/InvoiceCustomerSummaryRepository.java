/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.repository;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceCustomerSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Invoice customer summary repository.
 */
public interface InvoiceCustomerSummaryRepository extends ListCrudRepository<InvoiceCustomerSummaryEntity, Long> {
    /**
     * Find by invoice id optional.
     *
     * @param invoiceId the invoice id
     * @return the optional
     */
    Optional<InvoiceCustomerSummaryEntity> findByInvoiceId(Long invoiceId); // Useful for updates from Invoice events

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<InvoiceCustomerSummaryEntity> findByCustomerId(Long customerId);

    /**
     * Find by customer phone number optional.
     *
     * @param customerPhoneNumber the customer phone number
     * @return the optional
     */
    Optional<InvoiceCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber);
}
