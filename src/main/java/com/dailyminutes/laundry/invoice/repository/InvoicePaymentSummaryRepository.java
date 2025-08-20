/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.repository; // Updated package name

import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Invoice payment summary repository.
 */
public interface InvoicePaymentSummaryRepository extends ListCrudRepository<InvoicePaymentSummaryEntity, Long> {
    /**
     * Find by invoice id list.
     *
     * @param invoiceId the invoice id
     * @return the list
     */
    List<InvoicePaymentSummaryEntity> findByInvoiceId(Long invoiceId);

    /**
     * Find by payment id optional.
     *
     * @param paymentId the payment id
     * @return the optional
     */
    Optional<InvoicePaymentSummaryEntity> findByPaymentId(Long paymentId); // Useful for updates from Payment events

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<InvoicePaymentSummaryEntity> findByStatus(String status);

    /**
     * Find by method list.
     *
     * @param method the method
     * @return the list
     */
    List<InvoicePaymentSummaryEntity> findByMethod(String method);
}
