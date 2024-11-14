/*package tn.pfe.CnotConnectV1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.repository.BudgetAllocationRepository;
import tn.pfe.CnotConnectV1.repository.FinancialReportRepository;
import tn.pfe.CnotConnectV1.repository.InvoiceRepository;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;

@Service
public class ReconciliationService {
    
    @Autowired
    private SolidariteOlympiqueService solidariteOlympiqueService;
    
    @Autowired
    private FinancialReportRepository financialReportRepository;
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private BudgetAllocationRepository budgetAllocationRepository;
    
    public List<ReconciliationReport> reconcileFinancialReports(Long solidarityOlympicId) {
        List<ReconciliationReport> reconciliationReports = new ArrayList<>();
        
        SolidarityOlympic solidarityOlympic = projectRepository.findSolidarityOlympicById(solidarityOlympicId);
        List<Project> projects = projectRepository.findBySolidarityOlympic(solidarityOlympic);
        
        for (Project project : projects) {
            FinancialReport financialReport = financialReportRepository.findByProject(project);
            if (financialReport!= null) {
                double internalAccountingTotal = calculateInternalAccountingTotal(project);
                double financialReportTotal = calculateFinancialReportTotal(financialReport);
                
                ReconciliationReport reconciliationReport = new ReconciliationReport();
                reconciliationReport.setProgramId(solidarityOlympicId);
                reconciliationReport.setFinancialReportId(financialReport.getFinancial_report_id());
                reconciliationReport.setInternalAccountingTotal(internalAccountingTotal);
                reconciliationReport.setFinancialReportTotal(financialReportTotal);
                reconciliationReport.setReconciliationStatus(calculateReconciliationStatus(internalAccountingTotal, financialReportTotal));
                
                reconciliationReports.add(reconciliationReport);
            }
        }
        
        return reconciliationReports;
    }
    
    private double calculateInternalAccountingTotal(Project project) {
        double total = 0;
        List<BudgetAllocation> budgetAllocations = budgetAllocationRepository.findByProject(project);
        for (BudgetAllocation budgetAllocation : budgetAllocations) {
            total += budgetAllocation.getAllocatedAmount();
        }
        return total;
    }
    
    private double calculateFinancialReportTotal(FinancialReport financialReport) {
        double total = 0;
        List<Invoice> invoices = invoiceRepository.findByFinancialReport(financialReport);
        for (Invoice invoice : invoices) {
            total += invoice.getTotalAmount();
        }
        return total;
    }
    
    private ReconciliationStatus calculateReconciliationStatus(double internalAccountingTotal, double financialReportTotal) {
        if (internalAccountingTotal == financialReportTotal) {
            return ReconciliationStatus.RECONCILED;
        } else if (internalAccountingTotal > financialReportTotal) {
            return ReconciliationStatus.UNDER_REPORTED;
        } else {
            return ReconciliationStatus.OVER_REPORTED;
        }
    }
}
*/