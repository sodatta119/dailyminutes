/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.service;


import com.dailyminutes.laundry.customer.domain.event.*;
import com.dailyminutes.laundry.customer.domain.model.CustomerAddressEntity;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.dto.*;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final ApplicationEventPublisher events;

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        customerRepository.findByPhoneNumber(request.phoneNumber()).ifPresent(c -> {
            throw new IllegalArgumentException("Customer with phone number " + request.phoneNumber() + " already exists.");
        });
        customerRepository.findByEmail(request.email()).ifPresent(c -> {
            throw new IllegalArgumentException("Customer with email " + request.email() + " already exists.");
        });

        CustomerEntity customer = new CustomerEntity(null, request.subscriberId(), request.phoneNumber(), request.name(), request.email());
        CustomerEntity savedCustomer = customerRepository.save(customer);

        events.publishEvent(new CustomerCreatedEvent(savedCustomer.getId(), savedCustomer.getSubscriberId(), savedCustomer.getPhoneNumber(), savedCustomer.getName(), savedCustomer.getEmail()));

        return toCustomerResponse(savedCustomer);
    }

    public CustomerResponse updateCustomer(UpdateCustomerRequest request) {
        CustomerEntity existingCustomer = customerRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + request.id() + " not found."));

        existingCustomer.setSubscriberId(request.subscriberId());
        existingCustomer.setPhoneNumber(request.phoneNumber());
        existingCustomer.setName(request.name());
        existingCustomer.setEmail(request.email());

        CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);
        events.publishEvent(new CustomerUpdatedEvent(updatedCustomer.getId(), updatedCustomer.getSubscriberId(), updatedCustomer.getPhoneNumber(), updatedCustomer.getName(), updatedCustomer.getEmail()));
        return toCustomerResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found.");
        }
        customerRepository.deleteById(id);
        events.publishEvent(new CustomerDeletedEvent(id));
    }

    public CustomerAddressResponse addAddress(CreateCustomerAddressRequest request) {
        CustomerEntity customer = customerRepository.findById(request.customerId()).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with ID " + request.customerId() + " not found.");
        }
        CustomerAddressEntity address = new CustomerAddressEntity(null, request.customerId(), request.addressType(), request.isDefault(), request.flatApartment(), request.addressLine(), request.street(), request.city(), request.state(), request.zipCode(), request.country(), request.longitude(), request.latitude(), request.geofenceId());
        CustomerAddressEntity savedAddress = addressRepository.save(address);
        events.publishEvent(new CustomerAddressAddedEvent(
                savedAddress.getId(), savedAddress.getCustomerId(),
                customer.getName(), customer.getPhoneNumber(), // Add customer details
                savedAddress.getAddressType(), savedAddress.isDefault(),
                savedAddress.getAddressLine(), savedAddress.getCity(),
                savedAddress.getZipCode(), savedAddress.getGeofenceId()
        ));
        return toAddressResponse(savedAddress);
    }

    public CustomerAddressResponse updateAddress(UpdateCustomerAddressRequest request) {
        CustomerEntity customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new IllegalArgumentException("Address with Customer ID " + request.customerId() + " not found."));
        CustomerAddressEntity existingAddress = addressRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Address with ID " + request.id() + " not found."));

        existingAddress.setAddressType(request.addressType());
        existingAddress.setDefault(request.isDefault());
        existingAddress.setFlatApartment(request.flatApartment());
        existingAddress.setAddressLine(request.addressLine());
        existingAddress.setStreet(request.street());
        existingAddress.setCity(request.city());
        existingAddress.setState(request.state());
        existingAddress.setZipCode(request.zipCode());
        existingAddress.setCountry(request.country());
        existingAddress.setLongitude(request.longitude());
        existingAddress.setLatitude(request.latitude());
        existingAddress.setGeofenceId(request.geofenceId());

        CustomerAddressEntity updatedAddress = addressRepository.save(existingAddress);
        events.publishEvent(new CustomerAddressUpdatedEvent(
                updatedAddress.getId(), updatedAddress.getCustomerId(),
                customer.getName(), customer.getPhoneNumber(), // Add customer details
                updatedAddress.getAddressType(), updatedAddress.isDefault(),
                updatedAddress.getAddressLine(), updatedAddress.getCity(),
                updatedAddress.getZipCode(), updatedAddress.getGeofenceId()
        ));
        return toAddressResponse(updatedAddress);
    }

    public void removeAddress(Long addressId) {
        CustomerAddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address with ID " + addressId + " not found."));
        addressRepository.deleteById(addressId);
        events.publishEvent(new CustomerAddressRemovedEvent(address.getId(), address.getCustomerId()));
    }

    private CustomerResponse toCustomerResponse(CustomerEntity entity) {
        return new CustomerResponse(entity.getId(), entity.getSubscriberId(), entity.getPhoneNumber(), entity.getName(), entity.getEmail());
    }

    private CustomerAddressResponse toAddressResponse(CustomerAddressEntity entity) {
        return new CustomerAddressResponse(entity.getId(), entity.getCustomerId(), entity.getAddressType(), entity.isDefault(), entity.getFlatApartment(), entity.getAddressLine(), entity.getStreet(), entity.getCity(), entity.getState(), entity.getZipCode(), entity.getCountry(), entity.getLongitude(), entity.getLatitude(), entity.getGeofenceId());
    }
}
