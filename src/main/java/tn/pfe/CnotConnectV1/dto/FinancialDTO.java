package tn.pfe.CnotConnectV1.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.Project;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialDTO {
    private Long fReportId;
    private Date reportDate;
    private String reportPeriod;
    private String reportType;
    private double totalIncome;
    private double totalExpenditure;
    private double netIncome;
    private List<InvoiceDTO> invoices; 
    private List<BudgetAllocationDTO> budgetAllocations;
    private ProjectDTO project; 

}
