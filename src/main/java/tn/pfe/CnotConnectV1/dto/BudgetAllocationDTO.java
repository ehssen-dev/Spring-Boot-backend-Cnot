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
    private String budgetNumber;  
    private Double allocatedAmount;    
    private Double usedBudget = 0.0;;        
    private Double remainingBudget;     
    private Date startDate;             
    private Date endDate;              
    private BudgetStatus budgetStatus;  
    private Category category;          
    
    private Long requestId;  
    private Long financialReportId;     
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
    
 
}