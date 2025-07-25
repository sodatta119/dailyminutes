package com.dailyminutes.laundry.payment.service;

import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import com.dailyminutes.laundry.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentQueryServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentQueryService paymentQueryService;

    @Test
    void findPaymentById_shouldReturnPayment() {
        PaymentEntity payment = new PaymentEntity(1L, 1L, 1L, "txn1", BigDecimal.TEN, LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, "");
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThat(paymentQueryService.findPaymentById(1L)).isPresent();
    }
}
