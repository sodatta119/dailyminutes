package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.DailyminutesApplication; // Import the main application class
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.repository.OrderRepository;
import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.repository.StoreRepository;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // For generating unique IDs

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.payment.repository",
        "com.dailyminutes.laundry.customer.repository",
        "com.dailyminutes.laundry.order.repository",
        "com.dailyminutes.laundry.store.repository",
        "com.dailyminutes.laundry.agent.repository",
        "com.dailyminutes.laundry.team.repository"})
@ComponentScan(basePackages = {
        "com.dailyminutes.laundry.order.domain.model",
        "com.dailyminutes.laundry.customer.domain.model",
        "com.dailyminutes.laundry.store.domain.model",
        "com.dailyminutes.laundry.payment.domain.model",
        "com.dailyminutes.laundry.agent.domain.model",
        "com.dailyminutes.laundry.team.domain.model"
})
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AgentRepository agentRepository;


    TeamEntity team;
    AgentEntity manager;
    StoreEntity store;
    CustomerEntity customer;
    OrderEntity order;
    @BeforeEach
    void setup(){
        team=teamRepository.save(new TeamEntity(null, "Unique Team Name", "A unique team", TeamRole.OPS));
        manager = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "9876543210", "A001", LocalDate.now(), null, AgentDesignation.STORE_AGENT)); // Updated

        store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", manager.getId()));
        customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        order = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
    }

    // Helper method to generate unique transaction IDs for tests
    private String generateUniqueTransactionId() {
        return "TXN-" + UUID.randomUUID().toString();
    }

    @Test
    void testSaveAndFindPayment() {
        String transactionId = generateUniqueTransactionId();
        PaymentEntity payment = new PaymentEntity(
                null, order.getId(), customer.getId(), transactionId, new BigDecimal("150.00"),
                LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, "Online payment for order 1");
        PaymentEntity savedPayment = paymentRepository.save(payment);

        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getId()).isNotNull();
        assertThat(savedPayment.getOrderId()).isEqualTo(order.getId());
        assertThat(savedPayment.getCustomerId()).isEqualTo(customer.getId());
        assertThat(savedPayment.getTransactionId()).isEqualTo(transactionId);

        Optional<PaymentEntity> foundPayment = paymentRepository.findById(savedPayment.getId());
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getAmount()).isEqualByComparingTo("150.00");
        assertThat(foundPayment.get().getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void testUpdatePayment() {
        String transactionId = generateUniqueTransactionId();
        PaymentEntity payment = new PaymentEntity(
                null, order.getId(), customer.getId(), transactionId, new BigDecimal("200.00"),
                LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.UPI, "UPI payment for order 2");
        PaymentEntity savedPayment = paymentRepository.save(payment);

        savedPayment.setStatus(PaymentStatus.COMPLETED);
        savedPayment.setRemarks("UPI payment successful");
        PaymentEntity updatedPayment = paymentRepository.save(savedPayment);

        Optional<PaymentEntity> foundUpdatedPayment = paymentRepository.findById(updatedPayment.getId());
        assertThat(foundUpdatedPayment).isPresent();
        assertThat(foundUpdatedPayment.get().getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(foundUpdatedPayment.get().getRemarks()).isEqualTo("UPI payment successful");
    }

    @Test
    void testDeletePayment() {
        String transactionId = generateUniqueTransactionId();
        PaymentEntity payment = new PaymentEntity(
                null, order.getId(), customer.getId(), transactionId, new BigDecimal("50.00"),
                LocalDateTime.now(), PaymentStatus.FAILED, PaymentMethod.CASH, "Cash payment for order 3");
        PaymentEntity savedPayment = paymentRepository.save(payment);

        paymentRepository.deleteById(savedPayment.getId());
        Optional<PaymentEntity> deletedPayment = paymentRepository.findById(savedPayment.getId());
        assertThat(deletedPayment).isNotPresent();
    }

    @Test
    void testFindByOrderId() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "9876533210", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "9844543210", "Jane Doe", "jane1@example.com"));
        CustomerEntity customer3 = customerRepository.save(new CustomerEntity(null, "SUB126", "9876543550", "Jane Doe", "jane2@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        String transactionId1 = generateUniqueTransactionId();
        String transactionId2 = generateUniqueTransactionId();
        paymentRepository.save(new PaymentEntity(null, order1.getId(), customer1.getId(), transactionId1, new BigDecimal("10.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, order1.getId(), customer2.getId(), transactionId2, new BigDecimal("20.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.DEBIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, order2.getId(), customer3.getId(), generateUniqueTransactionId(), new BigDecimal("30.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.UPI, ""));

        List<PaymentEntity> paymentsForOrder100 = paymentRepository.findByOrderId(order1.getId());
        assertThat(paymentsForOrder100).hasSize(2);
        assertThat(paymentsForOrder100.stream().allMatch(p -> p.getOrderId().equals(order1.getId()))).isTrue();
    }

    @Test
    void testFindByCustomerId() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "9876533210", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "9844543210", "Jane Doe", "jane1@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        String transactionId3 = generateUniqueTransactionId();
        String transactionId4 = generateUniqueTransactionId();
        paymentRepository.save(new PaymentEntity(null, order1.getId(), customer1.getId(), transactionId3, new BigDecimal("40.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.NET_BANKING, ""));
        paymentRepository.save(new PaymentEntity(null, order2.getId(), customer1.getId(), transactionId4, new BigDecimal("50.00"), LocalDateTime.now(), PaymentStatus.REFUNDED, PaymentMethod.WALLET, ""));
        paymentRepository.save(new PaymentEntity(null, order3.getId(), customer2.getId(), generateUniqueTransactionId(), new BigDecimal("60.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));

        List<PaymentEntity> paymentsForCustomer30 = paymentRepository.findByCustomerId(customer1.getId());
        assertThat(paymentsForCustomer30).hasSize(2);
        assertThat(paymentsForCustomer30.stream().allMatch(p -> p.getCustomerId().equals(customer1.getId()))).isTrue();
    }

    @Test
    void testFindByTransactionId() {
        String uniqueTxnId = generateUniqueTransactionId();
        paymentRepository.save(new PaymentEntity(null, order.getId(), customer.getId(), uniqueTxnId, new BigDecimal("70.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.OTHER, ""));
        Optional<PaymentEntity> foundPayment = paymentRepository.findByTransactionId(uniqueTxnId);
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getAmount()).isEqualByComparingTo("70.00");
    }

    @Test
    void testFindByStatus() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "9876533210", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "9844543210", "Jane Doe", "jane1@example.com"));
        CustomerEntity customer3 = customerRepository.save(new CustomerEntity(null, "SUB126", "9876543550", "Jane Doe", "jane2@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        paymentRepository.save(new PaymentEntity(null, order1.getId(), customer1.getId(), generateUniqueTransactionId(), new BigDecimal("80.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, order2.getId(), customer2.getId(), generateUniqueTransactionId(), new BigDecimal("90.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.DEBIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, order3.getId(), customer3.getId(), generateUniqueTransactionId(), new BigDecimal("100.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.UPI, ""));

        List<PaymentEntity> completedPayments = paymentRepository.findByStatus(PaymentStatus.COMPLETED);
        assertThat(completedPayments).hasSize(2);
        assertThat(completedPayments.stream().allMatch(p -> p.getStatus().equals(PaymentStatus.COMPLETED))).isTrue();
    }

    @Test
    void testFindByMethod() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB124", "9876533210", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB125", "9844543210", "Jane Doe", "jane1@example.com"));
        CustomerEntity customer3 = customerRepository.save(new CustomerEntity(null, "SUB126", "9876543550", "Jane Doe", "jane2@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        paymentRepository.save(new PaymentEntity(null, order1.getId(), customer1.getId(), generateUniqueTransactionId(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentRepository.save(new PaymentEntity(null, order2.getId(), customer2.getId(), generateUniqueTransactionId(), new BigDecimal("120.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.CASH, ""));
        paymentRepository.save(new PaymentEntity(null, order3.getId(), customer3.getId(), generateUniqueTransactionId(), new BigDecimal("130.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, ""));

        List<PaymentEntity> cashPayments = paymentRepository.findByMethod(PaymentMethod.CASH);
        assertThat(cashPayments).hasSize(2);
        assertThat(cashPayments.stream().allMatch(p -> p.getMethod().equals(PaymentMethod.CASH))).isTrue();
    }
}
