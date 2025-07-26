package com.dailyminutes.laundry.order.repository;

import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderItemEntity;
import com.dailyminutes.laundry.order.domain.model.OrderItemMetadataEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
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
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = {
        "com.dailyminutes.laundry.order.repository"
})
@ComponentScan(basePackages = {
        "com.dailyminutes.laundry.order.domain.model"
})
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMetadataRepository orderItemMetadataRepository;

    /**
     * Test save and find order with items and metadata.
     */
    @Test
    void testSaveAndFindOrderWithItemsAndMetadata() {

        OrderEntity order = new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50"));
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
        assertThat(foundOrder.get().getCustomerId()).isEqualTo(10l); // Assert customer ID

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
        orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("10.00")));
        orderRepository.save(new OrderEntity(null, 20l, 10l, LocalDateTime.now(), OrderStatus.DELIVERED, new BigDecimal("20.00")));
        orderRepository.save(new OrderEntity(null, 30l, 30l, LocalDateTime.now(), OrderStatus.ACCEPTED, new BigDecimal("15.00")));

        List<OrderEntity> orders = orderRepository.findByCustomerId(10l); // Using customer ID
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getCustomerId()).isEqualTo(10l); // Assert customer ID
    }

    /**
     * Test find by store id.
     */
    @Test
    void testFindByStoreId() {
        orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("50.00")));
        orderRepository.save(new OrderEntity(null, 10l, 20l, LocalDateTime.now(), OrderStatus.ACCEPTED, new BigDecimal("75.00")));
        orderRepository.save(new OrderEntity(null, 30l, 30l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("30.00")));

        List<OrderEntity> orders = orderRepository.findByStoreId(10l);
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getStoreId()).isEqualTo(10l);
    }

    /**
     * Test update order.
     */
    @Test
    void testUpdateOrder() {
        OrderEntity order = new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("10.00"));
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
        OrderEntity order = new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("5.00"));
        OrderEntity savedOrder = orderRepository.save(order);

        // Create and save OrderItem
        OrderItemEntity item1 = new OrderItemEntity(null, savedOrder.getId(), 10l, new BigDecimal("1.0"), new BigDecimal("20.00"), "Item to delete");
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
