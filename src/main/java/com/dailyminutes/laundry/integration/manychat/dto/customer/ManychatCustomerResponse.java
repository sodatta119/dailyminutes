/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.integration.manychat.dto.customer;

import com.dailyminutes.laundry.integration.manychat.dto.ManyChatError;
import com.dailyminutes.laundry.integration.manychat.dto.Subscriber;

import java.util.List;

/**
 * The type Manychat customer response.
 */
public record ManychatCustomerResponse(String status,           // "success" or "error"
                                       List<Subscriber> data,
                                       ManyChatError error) {
}
