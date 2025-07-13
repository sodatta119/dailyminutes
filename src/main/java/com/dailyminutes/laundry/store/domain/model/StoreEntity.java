/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.store.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Store entity.
 */
@Table(name = "DL_STORE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreEntity {

    @Id
    private Long id;

    private String name;

    private String address;

    private String contactNumber;

    private String email;

    private Long managerId;
}