package com.chitchatfm.dailyminutes.laundry.invoice.repository;

import com.chitchatfm.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

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
}

