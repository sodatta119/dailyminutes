package com.chitchatfm.dailyminutes.laundry.invoice.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "DL_INVOICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "invoiceItems")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String swipeInvoiceId; // ID from the external Swipe system

    @Column(nullable = false, length = 20)
    private String customerPhoneNumber; // Logical link to the Customer module

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalTax;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDiscount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemEntity> invoiceItems = new ArrayList<>();

    public void addInvoiceItem(InvoiceItemEntity item) {
        invoiceItems.add(item);
        item.setInvoice(this);
    }

    public void removeInvoiceItem(InvoiceItemEntity item) {
        invoiceItems.remove(item);
        item.setInvoice(null);
    }
}

