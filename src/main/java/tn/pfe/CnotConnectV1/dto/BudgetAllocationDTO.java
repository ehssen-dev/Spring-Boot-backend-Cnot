package tn.pfe.CnotConnectV1.dto;

import java.util.Date;

import lombok.*;
import tn.pfe.CnotConnectV1.enums.BudgetStatus;
import tn.pfe.CnotConnectV1.enums.Category;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetAllocationDTO {

    private Long budgetId;       
    private String budgetNumber;  // ID of the budget allocation
    private Double allocatedAmount;     // Amount allocated
    private Double usedBudget = 0.0;;          // Amount used from the allocated budget
    private Double remainingBudget;     // Remaining amount in the budget
    private Date startDate;             // Start date of the budget allocation
    private Date endDate;               // End date of the budget allocation
    private BudgetStatus budgetStatus;  // Status of the budget (e.g., APPROVED, PENDING)
    private Category category;          // Category of the budget (e.g., OPERATING, CAPITAL)
    
    private Long requestId;  // Associated Procurement Request ID
    private Long financialReportId;      // Associated Financial Report ID
    private Long projectId; 
     
    public BudgetAllocationDTO(String budgetNumber, Double allocatedAmount, Double usedBudget, Double remainingBudget,
		            Date startDate, Date endDate, BudgetStatus budgetStatus, Category category) {
		this.budgetNumber = budgetNumber;
		this.allocatedAmount = allocatedAmount;
		this.usedBudget = usedBudget;
		this.remainingBudget = remainingBudget;
		this.startDate = startDate;
		this.endDate = endDate;
		this.budgetStatus = budgetStatus;
		this.category = category;
		}
    
    // Associated Project ID
}