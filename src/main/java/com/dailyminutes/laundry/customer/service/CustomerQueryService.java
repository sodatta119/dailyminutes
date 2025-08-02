/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.service;


import com.dailyminutes.laundry.customer.dto.CustomerAddressResponse;
import com.dailyminutes.laundry.customer.dto.CustomerResponse;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;

    public Optional<CustomerResponse> findCustomerById(Long id) {
        return customerRepository.findById(id).map(c -> new CustomerResponse(c.getId(), c.getSubscriberId(), c.getPhoneNumber(), c.getName(), c.getEmail()));
    }

    public List<CustomerResponse> findAllCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(c -> new CustomerResponse(c.getId(), c.getSubscriberId(), c.getPhoneNumber(), c.getName(), c.getEmail()))
                .collect(Collectors.toList());
    }

    public List<CustomerAddressResponse> findAddressesByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId).stream()
                .map(a -> new CustomerAddressResponse(a.getId(), a.getCustomerId(), a.getAddressType(), a.isDefault(), a.getFlatApartment(), a.getAddressLine(), a.getStreet(), a.getCity(), a.getState(), a.getZipCode(), a.getCountry(), a.getLongitude(), a.getLatitude(), a.getGeofenceId()))
                .collect(Collectors.toList());
    }
}
