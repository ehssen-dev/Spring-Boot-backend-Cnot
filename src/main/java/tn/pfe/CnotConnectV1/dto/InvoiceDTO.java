package tn.pfe.CnotConnectV1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    private Long invoiceId;
    private String invoiceNumber;
    private Double totalAmount;
    private Date invoiceDate;
    private Date dueDate;
    private boolean paid;
    private String description;
    private String status; 
    private Long budgetId; 
    private Long financialReportId;
    private Long projectId; 
    private List<PurchaseOrderDTO> purchaseOrders = new ArrayList<>();
    
    public List<Long> getPurchaseOrderIds() {
        return purchaseOrders.stream()
            .map(PurchaseOrderDTO::getPurchaseId)
            .collect(Collectors.toList());
    }
    public InvoiceDTO(Long invoiceId, String invoiceNumber, Double totalAmount, Date invoiceDate, Date dueDate,
                      boolean paid, String description, String status, Long budgetId, Long financialReportId,
                      Long projectId, List<PurchaseOrderDTO>purchaseOrders) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.totalAmount = totalAmount;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.paid = paid;
        this.description = description;
        this.status = status;
        this.budgetId = budgetId;
        this.financialReportId = financialReportId;
        this.projectId = projectId;
        this.purchaseOrders = purchaseOrders;
    }
}
