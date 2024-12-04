package tn.pfe.CnotConnectV1.dto;

import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.*;
import tn.pfe.CnotConnectV1.enums.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDTO {

    private Long purchaseId;
    private String orderNumber;
    @Min(1) 
    private int quantity;
    private String purchaseName;
    private String description;
    @NotNull(message = "Unit price cannot be null")
    @PositiveOrZero(message = "Unit price must be zero or positive")
    private Double unitPrice;
    private Double totalAmount;
    private Date purchaseDate;
    private Date expectedDeliveryDate; 
    private OrderStatus status; 
    private Long supplierId;
    private Long projectId;
    private Long invoiceId;

    public PurchaseOrderDTO(Long purchaseId, String orderNumber, int quantity, String purchaseName, 
            Double unitPrice, Double totalAmount, Date purchaseDate, 
            Long supplierId, Long projectId, Long invoiceId) {
this.purchaseId = purchaseId;
this.orderNumber = orderNumber;
this.quantity = quantity;
this.purchaseName = purchaseName;
this.unitPrice = unitPrice;
this.totalAmount = totalAmount;
this.purchaseDate = purchaseDate;
this.supplierId = supplierId;
this.projectId = projectId;
this.invoiceId = invoiceId;
}
    
}
