package com.chitchatfm.dailyminutes.laundry.customer.repository;

import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * The interface Customer repository.
 */
public interface CustomerRepository extends ListCrudRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findBySubscriberId(String subscriberId);

    Optional<CustomerEntity> findByEmail(String email);

    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber); // Added new finder method
}

