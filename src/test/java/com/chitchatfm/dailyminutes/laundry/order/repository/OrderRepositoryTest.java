package com.chitchatfm.dailyminutes.laundry.order.repository;

import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.chitchatfm.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.chitchatfm.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderItemEntity;
import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderItemMetadataEntity;
import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.chitchatfm.dailyminutes.laundry.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Order repository test.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {
        "com.chitchatfm.dailyminutes.laundry.order.repository",
        "com.chitchatfm.dailyminutes.laundry.customer.repository",
        "com.chitchatfm.dailyminutes.laundry.store.repository",
        "com.chitchatfm.dailyminutes.laundry.catalog.repository"
})
@ComponentScan(basePackages = {
        "com.chitchatfm.dailyminutes.laundry.order.domain.model",
        "com.chitchatfm.dailyminutes.laundry.customer.domain.model",
        "com.chitchatfm.dailyminutes.laundry.store.domain.model",
        "com.chitchatfm.dailyminutes.laundry.catalog.domain.model"
})
class OrderRepositoryTest {


    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMetadataRepository orderItemMetadataRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    /**
     * Test save and find order with items and metadata.
     */
    @Test
    void testSaveAndFindOrderWithItemsAndMetadata() {

        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));

        OrderEntity order = new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50"));
        OrderEntity savedOrder = orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();

        // 2. Create and save OrderItems, linking them to the saved Order's ID
        OrderItemEntity item1 = new OrderItemEntity(null, savedOrder.getId(), 101L, new BigDecimal("2.0"), new BigDecimal("10.00"), "Wash & Fold");
        OrderItemEntity item2 = new OrderItemEntity(null, savedOrder.getId(), 102L, new BigDecimal("1.0"), new BigDecimal("5.50"), "Ironing");
        OrderItemEntity savedItem1 = orderItemRepository.save(item1);
        OrderItemEntity savedItem2 = orderItemRepository.save(item2);

        // 3. Create and save OrderItemMetadata, linking them to the saved OrderItems' IDs
        OrderItemMetadataEntity metadata1_1 = new OrderItemMetadataEntity(null, savedItem1.getId(), "IMAGE", "http://example.com/img1.jpg", "Before wash image");
        OrderItemMetadataEntity metadata1_2 = new OrderItemMetadataEntity(null, savedItem1.getId(), "TEXT_NOTE", "Delicate cycle", "Special instruction for wash");
        orderItemMetadataRepository.save(metadata1_1);
        orderItemMetadataRepository.save(metadata1_2);

        // Verify the order itself
        Optional<OrderEntity> foundOrder = orderRepository.findById(savedOrder.getId());
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getCustomerId()).isEqualTo(customer.getId()); // Assert customer ID

        // Verify order items for this order
        List<OrderItemEntity> foundItems = orderItemRepository.findByOrderId(savedOrder.getId());
        assertThat(foundItems).hasSize(2);
        assertThat(foundItems.stream().map(OrderItemEntity::getCatalogId)).containsExactlyInAnyOrder(101L, 102L);

        // Verify metadata for item1
        List<OrderItemMetadataEntity> foundMetadata1 = orderItemMetadataRepository.findByOrderItemId(savedItem1.getId());
        assertThat(foundMetadata1).hasSize(2);
        assertThat(foundMetadata1.stream().map(OrderItemMetadataEntity::getType)).containsExactlyInAnyOrder("IMAGE", "TEXT_NOTE");
    }

    /**
     * Test find by customer id.
     */
    @Test
    void testFindByCustomerId() { // Updated test method name
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10L));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10L));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store3", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB124", "9876543211", "John Doe", "john@example.com"));

        orderRepository.save(new OrderEntity(null, store1.getId(), customer1.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("10.00")));
        orderRepository.save(new OrderEntity(null, store2.getId(), customer1.getId(), LocalDateTime.now(), OrderStatus.DELIVERED, new BigDecimal("20.00")));
        orderRepository.save(new OrderEntity(null, store3.getId(), customer2.getId(), LocalDateTime.now(), OrderStatus.ACCEPTED, new BigDecimal("15.00")));

        List<OrderEntity> orders = orderRepository.findByCustomerId(customer1.getId()); // Using customer ID
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getCustomerId()).isEqualTo(customer1.getId()); // Assert customer ID
    }

    /**
     * Test find by store id.
     */
    @Test
    void testFindByStoreId() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB124", "9876543211", "John Doe", "john@example.com"));
        CustomerEntity customer3 = customerRepository.save(new CustomerEntity(null, "SUB125", "9876543211", "Foo Bar", "fb@example.com"));
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10L));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10L));

        orderRepository.save(new OrderEntity(null, store1.getId(), customer1.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("50.00")));
        orderRepository.save(new OrderEntity(null, store1.getId(), customer2.getId(), LocalDateTime.now(), OrderStatus.ACCEPTED, new BigDecimal("75.00")));
        orderRepository.save(new OrderEntity(null, store2.getId(), customer3.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("30.00")));

        List<OrderEntity> orders = orderRepository.findByStoreId(store1.getId());
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getStoreId()).isEqualTo(store1.getId());
    }

    /**
     * Test update order.
     */
    @Test
    void testUpdateOrder() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order = new OrderEntity(null, store1.getId(), customer1.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("10.00"));
        OrderEntity savedOrder = orderRepository.save(order);

        savedOrder.setStatus(OrderStatus.DELIVERED);
        savedOrder.setTotalAmount(new BigDecimal("12.50"));
        orderRepository.save(savedOrder);

        Optional<OrderEntity> updatedOrder = orderRepository.findById(savedOrder.getId());
        assertThat(updatedOrder).isPresent();
        assertThat(updatedOrder.get().getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(updatedOrder.get().getTotalAmount()).isEqualByComparingTo("12.50");
    }

    /**
     * Test delete order and associated items.
     */
    @Test
    void testDeleteOrderAndAssociatedItems() {
        // Create and save Order
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        CatalogEntity catalog=catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));

        OrderEntity order = new OrderEntity(null, store1.getId(), customer1.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("5.00"));
        OrderEntity savedOrder = orderRepository.save(order);

        // Create and save OrderItem
        OrderItemEntity item1 = new OrderItemEntity(null, savedOrder.getId(), catalog.getId(), new BigDecimal("1.0"), new BigDecimal("20.00"), "Item to delete");
        OrderItemEntity savedItem1 = orderItemRepository.save(item1);

        // Create and save OrderItemMetadata
        OrderItemMetadataEntity metadata1 = new OrderItemMetadataEntity(null, savedItem1.getId(), "NOTE", "Delete this item's metadata", "Description");
        orderItemMetadataRepository.save(metadata1);

        // Verify they exist
        assertThat(orderRepository.findById(savedOrder.getId())).isPresent();
        assertThat(orderItemRepository.findByOrderId(savedOrder.getId())).hasSize(1);
        assertThat(orderItemMetadataRepository.findByOrderItemId(savedItem1.getId())).hasSize(1);


        // Delete the parent Order
        orderRepository.deleteById(savedOrder.getId());

        // Verify Order is gone
        assertThat(orderRepository.findById(savedOrder.getId())).isNotPresent();

        // Verify associated OrderItems are gone (assuming database CASCADE DELETE or manual cleanup in service)
        // Spring Data JDBC does NOT automatically cascade deletes for child entities.
        // You would typically handle this in your service layer or via database foreign key constraints.
        assertThat(orderItemRepository.findByOrderId(savedOrder.getId())).isEmpty();
        assertThat(orderItemMetadataRepository.findByOrderItemId(savedItem1.getId())).isEmpty();
    }
}
