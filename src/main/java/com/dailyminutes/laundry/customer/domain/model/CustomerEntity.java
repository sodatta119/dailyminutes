/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */

package com.dailyminutes.laundry.customer.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Customer entity.
 */
@Table(name = "DL_CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerEntity {

    @Id
    private Long id;

    private String subscriberId;

    private String phoneNumber;

    private String name;

    private String email;

}

