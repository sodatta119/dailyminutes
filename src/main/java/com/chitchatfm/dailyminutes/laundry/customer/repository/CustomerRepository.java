package com.chitchatfm.dailyminutes.laundry.customer.repository;

import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Customer repository.
 */
public interface CustomerRepository extends ListCrudRepository<CustomerEntity, Long> {
}

