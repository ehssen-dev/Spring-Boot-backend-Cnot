package tn.pfe.CnotConnectV1.entities;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import tn.pfe.CnotConnectV1.enums.OrderStatus;



@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseId;
    
    @Column( name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @Min(1) 
    private int quantity; 
 
    @Column(name = "purchase_Name")
    private String purchaseName; 

    private String description;

    @NotNull(message = "Unit price cannot be null")
    @PositiveOrZero(message = "Unit price must be zero or positive")
    @Column(name = "unit_Price")
    private Double unitPrice;

    @Column(name = "total_Amount")
    private Double totalAmount;

    @Column(name = "purchase_Date")
    private Date purchaseDate;

    @Column(name = "expected_Delivery_Date")
    private Date expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;

    @PrePersist
    @PreUpdate
    private void calculateTotalAmount() {
        if (unitPrice != null && quantity > 0) {
            this.totalAmount = unitPrice * quantity;
        }
    }
    
    public Long getSupplierId() {
        return (supplier != null) ? supplier.getSupplierId() : null;
    }

    // Getter method to get Project ID
    public Long getProjectId() {
        return (project != null) ? project.getProjectId() : null;
    }
    public Long getInvoiceId() {
        return (invoice != null) ? invoice.getInvoiceId() : null;
    }
    
}
