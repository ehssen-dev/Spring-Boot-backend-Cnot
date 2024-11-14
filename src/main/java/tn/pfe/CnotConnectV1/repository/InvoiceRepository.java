package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.enums.InvoiceStatus;



@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("SELECT i FROM Invoice i WHERE i.dueDate >= CURRENT_DATE")
    List<Invoice> findIncomingInvoices();
    List<Invoice> findByProject(Project project);
    List<Invoice> findByBudget(BudgetAllocation budget);
    List<Invoice> findByFinancialReport(FinancialReport financialReport);
    List<Invoice> findByStatus(InvoiceStatus status);
    
    List<Invoice> findByInvoiceDateBetween(Date startDate, Date endDate);
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.purchaseOrders LEFT JOIN FETCH i.project WHERE i.invoiceId = :invoiceId")
    Optional<Invoice> findByIdWithDetails(@Param("invoiceId") Long invoiceId);
}

