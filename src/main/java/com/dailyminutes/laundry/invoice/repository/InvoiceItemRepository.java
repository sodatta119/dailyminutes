/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.invoice.repository;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceItemEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * The interface Invoice item repository.
 */
public interface InvoiceItemRepository extends ListCrudRepository<InvoiceItemEntity, Long> {

    /**
     * Find by invoice id list.
     *
     * @param id the id
     * @return the list
     */
    List<InvoiceItemEntity> findByInvoiceId(Long id);
}

