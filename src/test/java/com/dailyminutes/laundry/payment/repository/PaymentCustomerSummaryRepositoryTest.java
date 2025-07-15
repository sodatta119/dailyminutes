package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.payment.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.payment.domain.model") // Updated package name
class PaymentCustomerSummaryRepositoryTest {

    @Autowired
    private PaymentCustomerSummaryRepository paymentCustomerSummaryRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    private String generateUniquePhoneNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    private String generateUniqueTransactionNumber() {
        return "TRN_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    private String generateUniqueEmail() {
        return "cust_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test
    void testSaveAndFindPaymentCustomerSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        String phoneNumber = generateUniquePhoneNumber();
        String email = generateUniqueEmail();

        PaymentCustomerSummaryEntity summary = new PaymentCustomerSummaryEntity(
                null, payment.getId(), 10l, "Customer A", phoneNumber, email);
        PaymentCustomerSummaryEntity savedSummary = paymentCustomerSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getPaymentId()).isEqualTo(payment.getId());
        assertThat(savedSummary.getCustomerId()).isEqualTo(10l);
        assertThat(savedSummary.getCustomerName()).isEqualTo("Customer A");
        assertThat(savedSummary.getCustomerPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedSummary.getCustomerEmail()).isEqualTo(email);

        Optional<PaymentCustomerSummaryEntity> foundSummary = paymentCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Customer A");
    }

    @Test
    void testUpdatePaymentCustomerSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        String phoneNumber = generateUniquePhoneNumber();
        String email = generateUniqueEmail();

        PaymentCustomerSummaryEntity summary = new PaymentCustomerSummaryEntity(
                null, payment.getId(), 10l, "Customer B", phoneNumber, email);
        PaymentCustomerSummaryEntity savedSummary = paymentCustomerSummaryRepository.save(summary);

        savedSummary.setCustomerName("Customer B Updated");
        savedSummary.setCustomerEmail(generateUniqueEmail());
        paymentCustomerSummaryRepository.save(savedSummary);

        Optional<PaymentCustomerSummaryEntity> updatedSummary = paymentCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getCustomerName()).isEqualTo("Customer B Updated");
        assertThat(updatedSummary.get().getCustomerEmail()).isEqualTo(savedSummary.getCustomerEmail());
    }

    @Test
    void testDeletePaymentCustomerSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        String phoneNumber = generateUniquePhoneNumber();
        String email = generateUniqueEmail();

        PaymentCustomerSummaryEntity summary = new PaymentCustomerSummaryEntity(
                null, payment.getId(), 10l, "Customer C", phoneNumber, email);
        PaymentCustomerSummaryEntity savedSummary = paymentCustomerSummaryRepository.save(summary);

        paymentCustomerSummaryRepository.deleteById(savedSummary.getId());
        Optional<PaymentCustomerSummaryEntity> deletedSummary = paymentCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByPaymentId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentCustomerSummaryRepository.save(new PaymentCustomerSummaryEntity(null, payment1.getId(), 10l, "Cust D", generateUniquePhoneNumber(), generateUniqueEmail()));
        paymentCustomerSummaryRepository.save(new PaymentCustomerSummaryEntity(null, payment2.getId(), 20l, "Cust E", generateUniquePhoneNumber(), generateUniqueEmail()));

        Optional<PaymentCustomerSummaryEntity> foundSummary = paymentCustomerSummaryRepository.findByPaymentId(payment1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust D");
    }

    @Test
    void testFindByCustomerId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment3 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentCustomerSummaryRepository.save(new PaymentCustomerSummaryEntity(null, payment1.getId(), 10l, "Cust F", generateUniquePhoneNumber(), generateUniqueEmail()));
        paymentCustomerSummaryRepository.save(new PaymentCustomerSummaryEntity(null, payment2.getId(), 10l, "Cust G", generateUniquePhoneNumber(), generateUniqueEmail())); // Same customer, different payment
        paymentCustomerSummaryRepository.save(new PaymentCustomerSummaryEntity(null, payment3.getId(), 20l, "Cust H", generateUniquePhoneNumber(), generateUniqueEmail()));

        List<PaymentCustomerSummaryEntity> summariesForCustomer = paymentCustomerSummaryRepository.findByCustomerId(10l);
        assertThat(summariesForCustomer).hasSize(2);
        assertThat(summariesForCustomer.stream().allMatch(s -> s.getCustomerId().equals(10l))).isTrue();
    }

    @Test
    void testFindByCustomerPhoneNumber() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        String phoneNumber = generateUniquePhoneNumber();
        paymentCustomerSummaryRepository.save(new PaymentCustomerSummaryEntity(null, payment1.getId(), 10l, "Cust I", phoneNumber, generateUniqueEmail()));

        Optional<PaymentCustomerSummaryEntity> foundSummary = paymentCustomerSummaryRepository.findByCustomerPhoneNumber(phoneNumber);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust I");
    }
}
