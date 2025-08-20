/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.service;

import com.dailyminutes.laundry.payment.dto.*;
import com.dailyminutes.laundry.payment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Payment query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryService {

    private final PaymentRepository paymentRepository;

    /**
     * Find payment by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<PaymentResponse> findPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(p -> new PaymentResponse(p.getId(), p.getOrderId(), p.getCustomerId(), p.getTransactionId(), p.getAmount(), p.getPaymentDateTime(), p.getStatus(), p.getMethod(), p.getRemarks()));
    }

    /**
     * Find all payments list.
     *
     * @return the list
     */
    public List<PaymentResponse> findAllPayments() {
        return StreamSupport.stream(paymentRepository.findAll().spliterator(), false)
                .map(p -> new PaymentResponse(p.getId(), p.getOrderId(), p.getCustomerId(), p.getTransactionId(), p.getAmount(), p.getPaymentDateTime(), p.getStatus(), p.getMethod(), p.getRemarks()))
                .collect(Collectors.toList());
    }
}