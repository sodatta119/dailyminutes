package com.dailyminutes.laundry.payment.service;

import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import com.dailyminutes.laundry.payment.dto.CreatePaymentRequest;
import com.dailyminutes.laundry.payment.dto.UpdatePaymentRequest;
import com.dailyminutes.laundry.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPayment_shouldCreateAndPublishEvent() {
        CreatePaymentRequest request = new CreatePaymentRequest(1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        PaymentEntity payment = new PaymentEntity(1L, 1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        when(paymentRepository.save(any())).thenReturn(payment);

        paymentService.createPayment(request);

        verify(events).publishEvent(any(PaymentMadeEvent.class));
    }

    @Test
    void updatePayment_shouldUpdateAndPublishEvent() {
        UpdatePaymentRequest request = new UpdatePaymentRequest(1L, 1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        PaymentEntity payment = new PaymentEntity(1L, 1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.CASH, "");
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenReturn(payment);

        paymentService.updatePayment(request);

        verify(events).publishEvent(any(PaymentMadeEvent.class));
    }

    @Test
    void deletePayment_shouldDelete() {
        when(paymentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(paymentRepository).deleteById(1L);

        paymentService.deletePayment(1L);

        verify(paymentRepository).deleteById(1L);
    }

    @Test
    void updatePayment_shouldThrowException_whenPaymentNotFound() {
        UpdatePaymentRequest request = new UpdatePaymentRequest(1L, 1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePayment(request));
    }
}