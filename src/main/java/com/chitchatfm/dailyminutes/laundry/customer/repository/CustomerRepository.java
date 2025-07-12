/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.customer.repository;

import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * The interface Customer repository.
 */
public interface CustomerRepository extends ListCrudRepository<CustomerEntity, Long> {
    /**
     * Find by subscriber id optional.
     *
     * @param subscriberId the subscriber id
     * @return the optional
     */
    Optional<CustomerEntity> findBySubscriberId(String subscriberId);

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<CustomerEntity> findByEmail(String email);

    /**
     * Find by phone number optional.
     *
     * @param phoneNumber the phone number
     * @return the optional
     */
    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber); // Added new finder method
}

