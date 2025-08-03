/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.repository;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceCustomerSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceCustomerSummaryRepository extends ListCrudRepository<InvoiceCustomerSummaryEntity, Long> {
    Optional<InvoiceCustomerSummaryEntity> findByInvoiceId(Long invoiceId); // Useful for updates from Invoice events

    List<InvoiceCustomerSummaryEntity> findByCustomerId(Long customerId);

    Optional<InvoiceCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber);
}
