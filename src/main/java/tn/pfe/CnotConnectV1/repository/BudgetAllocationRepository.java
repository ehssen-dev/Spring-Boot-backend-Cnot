package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.*;
import tn.pfe.CnotConnectV1.enums.*;


@Repository
public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, Long> {
	List<BudgetAllocation> findByProject(Project project);
    List<BudgetAllocation> findByCategory(Category category);
    List<BudgetAllocation> findByBudgetStatus(BudgetStatus budgetStatus);
    List<BudgetAllocation> findByStartDateBetween(Date startDate, Date endDate);
    List<BudgetAllocation> findByEndDateBetween(Date startDate, Date endDate);
    List<BudgetAllocation> findByStartDateAfter(Date startDate);
    List<BudgetAllocation> findByEndDateBefore(Date endDate);
    List<BudgetAllocation> findByAllocatedAmountGreaterThan(Double amount);
    List<BudgetAllocation> findByAllocatedAmountLessThan(Double amount);
    List<BudgetAllocation> findByProcurementRequest(ProcurementRequest procurementRequest);
    List<BudgetAllocation> findByFinancialReport(FinancialReport financialReport);
    
    List<BudgetAllocation> findByStartDateBetweenAndEndDateBetween(Date startDate, Date endDate, Date startDate2, Date endDate2);

}


