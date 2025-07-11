package com.chitchatfm.dailyminutes.laundry.invoice.repository;

import com.chitchatfm.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.chitchatfm.dailyminutes.laundry.invoice.domain.model.InvoiceItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

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

