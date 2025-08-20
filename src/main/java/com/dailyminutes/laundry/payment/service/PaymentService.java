/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.service;

import com.dailyminutes.laundry.payment.domain.event.PaymentFailedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentRefundedEvent;
import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import com.dailyminutes.laundry.payment.dto.CreatePaymentRequest;
import com.dailyminutes.laundry.payment.dto.PaymentResponse;
import com.dailyminutes.laundry.payment.dto.UpdatePaymentRequest;
import com.dailyminutes.laundry.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Payment service.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher events;

    /**
     * Create payment payment response.
     *
     * @param request the request
     * @return the payment response
     */
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        PaymentEntity payment = new PaymentEntity(null, request.orderId(), request.customerId(), request.transactionId(), request.amount(), request.paymentDateTime(), request.status(), request.method(), request.remarks());
        PaymentEntity savedPayment = paymentRepository.save(payment);

        publishPaymentEvents(savedPayment, null);

        return toPaymentResponse(savedPayment);
    }

    /**
     * Update payment payment response.
     *
     * @param request the request
     * @return the payment response
     */
    public PaymentResponse updatePayment(UpdatePaymentRequest request) {
        PaymentEntity existingPayment = paymentRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Payment with ID " + request.id() + " not found."));

        var oldStatus = existingPayment.getStatus();

        existingPayment.setOrderId(request.orderId());
        existingPayment.setCustomerId(request.customerId());
        existingPayment.setTransactionId(request.transactionId());
        existingPayment.setAmount(request.amount());
        existingPayment.setPaymentDateTime(request.paymentDateTime());
        existingPayment.setStatus(request.status());
        existingPayment.setMethod(request.method());
        existingPayment.setRemarks(request.remarks());

        PaymentEntity updatedPayment = paymentRepository.save(existingPayment);

        publishPaymentEvents(updatedPayment, oldStatus);

        return toPaymentResponse(updatedPayment);
    }

    /**
     * Delete payment.
     *
     * @param id the id
     */
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment with ID " + id + " not found.");
        }
        paymentRepository.deleteById(id);
    }

    private void publishPaymentEvents(PaymentEntity payment, PaymentStatus oldStatus) {
        if (oldStatus == payment.getStatus()) {
            return; // No status change, no event
        }

        switch (payment.getStatus()) {
            case COMPLETED:
                events.publishEvent(new PaymentMadeEvent(
                        payment.getId(),
                        payment.getOrderId(),
                        payment.getCustomerId(),
                        payment.getAmount(),
                        payment.getMethod().name(),
                        payment.getTransactionId(),
                        payment.getPaymentDateTime()
                ));
                break;
            case FAILED:
                events.publishEvent(new PaymentFailedEvent(payment.getId(), payment.getOrderId(), "Payment failed"));
                break;
            case REFUNDED:
                events.publishEvent(new PaymentRefundedEvent(payment.getId(), payment.getOrderId(), payment.getAmount()));
                break;
            default:
                break;
        }
    }

    private PaymentResponse toPaymentResponse(PaymentEntity entity) {
        return new PaymentResponse(entity.getId(), entity.getOrderId(), entity.getCustomerId(), entity.getTransactionId(), entity.getAmount(), entity.getPaymentDateTime(), entity.getStatus(), entity.getMethod(), entity.getRemarks());
    }
}