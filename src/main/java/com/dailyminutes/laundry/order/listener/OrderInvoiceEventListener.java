/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.order.domain.event.OrderInfoRequestEvent;
import com.dailyminutes.laundry.order.domain.event.OrderInfoResponseEvent;
import com.dailyminutes.laundry.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderInvoiceEventListener {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onCustomerIdForOrderRequested(OrderInfoRequestEvent event) {
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            events.publishEvent(new OrderInfoResponseEvent(
                    order.getId(),
                    order.getCustomerId(),
                    event.originalEvent()
            ));
        });
    }
}