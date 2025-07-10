package com.chitchatfm.dailyminutes.laundry.order.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Enum for the status of an order
enum OrderStatus {
    PENDING,
    ACCEPTED,
    IN_PROCESS,
    READY_FOR_PICKUP,
    DELIVERED,
    CANCELLED
}

@Entity
@Table(name = "DL_ORDER") // Table name prefixed with DM_
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orderItems") // Exclude lazy-loaded collections from toString to prevent issues
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // orderId

    // Logical link to the Store module's entity
    @Column(nullable = false)
    private Long storeId;

    // Logical link to the Customer module's entity (using phoneNumber as PK)
    @Column(nullable = false, length = 20)
    private String customerId;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 10, scale = 2) // Total amount for the order
    private BigDecimal totalAmount;

    // One-to-Many relationship with OrderItemEntity within the SAME module (Order aggregate)
    // CascadeType.ALL ensures that saving/deleting an Order also affects its OrderItems
    // orphanRemoval = true ensures that if an OrderItem is removed from the list, it's deleted from DB
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    // Helper method to add items and maintain bi-directional relationship
    public void addOrderItem(OrderItemEntity item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // Helper method to remove items
    public void removeOrderItem(OrderItemEntity item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}
