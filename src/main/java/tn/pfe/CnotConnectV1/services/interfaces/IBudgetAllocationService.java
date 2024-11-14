package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.dto.BudgetAllocationDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.enums.BudgetStatus;

public interface IBudgetAllocationService {

	BudgetAllocation getBudgetAllocationById(Long budgetId);

	void deleteBudgetAllocation(Long budgetId);

	///BudgetAllocation updateBudgetAllocation(Long budgetId, BudgetAllocation updatedBudgetAllocation);


	void updateBudgetStatus(BudgetAllocation budgetAllocation, BudgetStatus newStatus);

//	BudgetAllocation createBudgetAllocation(BudgetAllocation budgetAllocation);

	Double calculateTotalBudgetForProject(Long projectId);


	BudgetAllocationDTO addBudgetAllocation(BudgetAllocationDTO dto);


	BudgetAllocationDTO updateBudgetAllocation(Long budgetId, BudgetAllocationDTO dto);



	BudgetAllocation allocateBudget(Long budgetId, BudgetAllocationDTO dto);

	BudgetAllocation allocateBudgetB(BudgetAllocationDTO dto);

	void addExpenseToBudget(Long budgetId, Double expenseAmount);

	List<BudgetAllocationDTO> getAllBudgetAllocations();

}
