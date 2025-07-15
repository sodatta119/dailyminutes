/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.order.domain.model; // Updated package name

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Store summary information relevant to the Order module.
 * This entity is populated via events from the Store module.
 */
@Table("DL_ORDER_STORE_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderStoreSummaryEntity {

    @Id
    private Long id;

    private Long orderId;

    private Long storeId;

    private String storeName;

    private String storeAddress;

    private String storeContactNumber;

    private String storeEmail;
}
