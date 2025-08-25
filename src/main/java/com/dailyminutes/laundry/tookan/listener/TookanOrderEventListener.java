/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/08/25
 */
package com.dailyminutes.laundry.tookan.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TookanOrderEventListener {

//    private static final Logger logger = LoggerFactory.getLogger(TookanOrderEventListener.class);
//    private final TookanApiClient tookanApiClient;
//    private final TookanProperties properties;
//    private final CustomerQueryService customerQueryService;
//    private final StoreQueryService storeQueryService;
//
//    @ApplicationModuleListener
//    public void onOrderCreated(OrderCreatedEvent event) {
//        logger.info("Received OrderCreatedEvent for orderId: {}. Preparing to create Tookan task.", event.orderId());
//
//        // Fetch required details using Query Services (a valid cross-module interaction)
//        var customerOpt = customerQueryService.findCustomerById(event.customerId());
//        var storeOpt = storeQueryService.findStoreById(event.storeId());
//
//        if (customerOpt.isEmpty() || storeOpt.isEmpty()) {
//            logger.error("Could not find customer or store for orderId: {}. Aborting Tookan task creation.", event.orderId());
//            return;
//        }
//
//        CustomerResponse customer = customerOpt.get();
//        StoreResponse store = storeOpt.get();
//
//        // Create the request payload for the Tookan API
//        CreateTookanTaskRequest tookanRequest = new CreateTookanTaskRequest(
//                properties.getApiKey(),
//                event.orderId().toString(),
//                null, // team_id - can be set if you have team mapping
//                1, // auto_assignment
//                "Laundry Order Pickup & Delivery",
//                customer.phoneNumber(),
//                customer.name(),
//                "Customer Address Placeholder", // TODO: Fetch customer's default address
//                event.orderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
//                event.orderDate().plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), // Example delivery time
//                1, // has_pickup
//                1, // has_delivery
//                0, // layout_type
//                1, // tracking_link
//                "+0530", // timezone for India
//                null, // fleet_id - can be set if you have agent mapping
//                Collections.emptyList()
//        );
//
//        tookanApiClient.createTaskInTookan(tookanRequest);
//    }
}