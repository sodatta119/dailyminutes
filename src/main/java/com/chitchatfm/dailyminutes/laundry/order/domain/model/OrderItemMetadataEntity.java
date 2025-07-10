package com.chitchatfm.dailyminutes.laundry.order.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DL_ORDER_ITEM_METADATA") // New table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orderItem")
public class OrderItemMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItemEntity orderItem;

    @Column(nullable = false)
    private String type; // e.g., "IMAGE", "TEXT_NOTE", "SPECIAL_INSTRUCTION"

    @Column(columnDefinition = "TEXT")
    private String value; // Content of the metadata (e.g., image URL, text)

    private String description; // Optional description for the metadata
}

