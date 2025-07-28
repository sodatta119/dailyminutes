/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.service;

import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceUpdatedEvent;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceItemEntity;
import com.dailyminutes.laundry.invoice.dto.CreateInvoiceRequest;
import com.dailyminutes.laundry.invoice.dto.InvoiceItemDto;
import com.dailyminutes.laundry.invoice.dto.InvoiceResponse;
import com.dailyminutes.laundry.invoice.dto.UpdateInvoiceRequest;
import com.dailyminutes.laundry.invoice.repository.InvoiceItemRepository;
import com.dailyminutes.laundry.invoice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ApplicationEventPublisher events;

    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        InvoiceEntity invoice = new InvoiceEntity(null, request.swipeInvoiceId(), request.customerId(), request.invoiceDate(), request.totalPrice(), request.totalTax(), request.totalDiscount());
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        List<InvoiceItemEntity> items = request.items().stream()
                .map(itemDto -> new InvoiceItemEntity(null, savedInvoice.getId(), itemDto.catalogId(), itemDto.quantity(), itemDto.itemPrice(), itemDto.tax()))
                .collect(Collectors.toList());
        invoiceItemRepository.saveAll(items);

        events.publishEvent(new InvoiceCreatedEvent(savedInvoice.getId(), savedInvoice.getOrderId(), savedInvoice.getInvoiceDate(), savedInvoice.getTotalPrice()));

        return toInvoiceResponse(savedInvoice, items);
    }

    public InvoiceResponse updateInvoice(UpdateInvoiceRequest request) {
        InvoiceEntity existingInvoice = invoiceRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + request.id() + " not found."));

        existingInvoice.setSwipeInvoiceId(request.swipeInvoiceId());
        existingInvoice.setOrderId(request.orderId());
        existingInvoice.setInvoiceDate(request.invoiceDate());
        existingInvoice.setTotalPrice(request.totalPrice());
        existingInvoice.setTotalTax(request.totalTax());
        existingInvoice.setTotalDiscount(request.totalDiscount());
        InvoiceEntity updatedInvoice = invoiceRepository.save(existingInvoice);

        invoiceItemRepository.deleteAll(invoiceItemRepository.findByInvoiceId(updatedInvoice.getId()));
        List<InvoiceItemEntity> items = request.items().stream()
                .map(itemDto -> new InvoiceItemEntity(null, updatedInvoice.getId(), itemDto.catalogId(), itemDto.quantity(), itemDto.itemPrice(), itemDto.tax()))
                .collect(Collectors.toList());
        invoiceItemRepository.saveAll(items);

        events.publishEvent(new InvoiceUpdatedEvent(updatedInvoice.getId(), updatedInvoice.getTotalPrice()));

        return toInvoiceResponse(updatedInvoice, items);
    }

    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Invoice with ID " + id + " not found.");
        }
        invoiceItemRepository.deleteAll(invoiceItemRepository.findByInvoiceId(id));
        invoiceRepository.deleteById(id);
        events.publishEvent(new InvoiceDeletedEvent(id));
    }

    private InvoiceResponse toInvoiceResponse(InvoiceEntity invoice, List<InvoiceItemEntity> items) {
        List<InvoiceItemDto> itemDtos = items.stream()
                .map(item -> new InvoiceItemDto(item.getId(), item.getCatalogId(), item.getQuantity(), item.getItemPrice(), item.getTax()))
                .collect(Collectors.toList());
        return new InvoiceResponse(invoice.getId(), invoice.getSwipeInvoiceId(), invoice.getOrderId(), invoice.getInvoiceDate(), invoice.getTotalPrice(), invoice.getTotalTax(), invoice.getTotalDiscount(), itemDtos);
    }
}
