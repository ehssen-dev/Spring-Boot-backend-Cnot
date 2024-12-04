package tn.pfe.CnotConnectV1.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.BudgetAllocationDTO;
import tn.pfe.CnotConnectV1.dto.DepartmentDTO;
import tn.pfe.CnotConnectV1.dto.FinancialDTO;
import tn.pfe.CnotConnectV1.dto.InvoiceDTO;
import tn.pfe.CnotConnectV1.dto.ProjectDTO;
import tn.pfe.CnotConnectV1.dto.PurchaseOrderDTO;
import tn.pfe.CnotConnectV1.dto.SolidarityOlympicDTO;
import tn.pfe.CnotConnectV1.dto.UserDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.TechnicalReport;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.BudgetAllocationRepository;
import tn.pfe.CnotConnectV1.repository.FinancialReportRepository;
import tn.pfe.CnotConnectV1.repository.InvoiceRepository;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IAthleteService;
import tn.pfe.CnotConnectV1.services.interfaces.IFinancialReportService;

@Service
public class FinancialReportService implements IFinancialReportService{

    private final InvoiceRepository invoiceRepository;
    private final BudgetAllocationRepository budgetAllocationRepository;
    private final FinancialReportRepository financialReportRepository;
    private final ProjectRepository projectRepository;
    @Autowired
    private  FinancialReportRepository financialRepository;

    
    @Autowired
    private ProjectService projectService;
    public FinancialReportService(InvoiceRepository invoiceRepository, 
                                  BudgetAllocationRepository budgetAllocationRepository, FinancialReportRepository financialReportRepository, ProjectRepository projectRepository) {
        this.invoiceRepository = invoiceRepository;
        this.budgetAllocationRepository = budgetAllocationRepository;
		this.financialReportRepository = financialReportRepository;
		this.projectRepository = projectRepository;
    }
 
    private static final Logger logger = LoggerFactory.getLogger(FinancialReportService.class);

    @Override
    public Map<String, Double> getFinancialReportChartData(Long projectId, Date startDate, Date endDate) {
     
        List<Invoice> invoices = invoiceRepository.findByInvoiceDateBetween(startDate, endDate);
        List<BudgetAllocation> budgetAllocations = budgetAllocationRepository.findByStartDateBetweenAndEndDateBetween(startDate, endDate, startDate, endDate);

        double totalIncome = invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();
        double totalExpenditure = budgetAllocations.stream().mapToDouble(BudgetAllocation::getAllocatedAmount).sum();

   
        Map<String, Double> chartData = new HashMap<>();
        chartData.put("Total Income", totalIncome);
        chartData.put("Total Expenditure", totalExpenditure);

        return chartData;
    }
    
    @Override
    public List<FinancialReport> getFinancialReportsByProjectId(Long projectId) {
     
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

     
        List<FinancialReport> financialReports = financialReportRepository.findByProject_ProjectId(projectId);
        
        if (financialReports.isEmpty()) {
            throw new ResourceNotFoundException("No financial reports found for Project ID: " + projectId);
        }
        
        return financialReports;
    }

    @Override
    public List<FinancialReport> findAll() {
        return financialReportRepository.findAll();
    }

    @Override
    public Optional<FinancialReport> findById(Long fReportId) {
        return financialReportRepository.findById(fReportId);
    }
    
    @Override
    public FinancialDTO generateFinancialReportForPeriod(Date startDate, Date endDate, Long projectId) {
        List<Invoice> invoices = invoiceRepository.findByInvoiceDateBetween(startDate, endDate);
        List<BudgetAllocation> budgetAllocations = budgetAllocationRepository.findByStartDateBetweenAndEndDateBetween(startDate, endDate, startDate, endDate);
        Project project = projectService.getProjectById(projectId);

        if (project == null) {
            throw new ResourceNotFoundException("Project not found with ID: " + projectId);
        }

        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                                                .map(this::convertToInvoiceDTO)
                                                .collect(Collectors.toList());

        List<BudgetAllocationDTO> budgetAllocationDTOs = budgetAllocations.stream()
                                                                           .map(this::convertToBudgetAllocationDTO)
                                                                           .collect(Collectors.toList());

        ProjectDTO projectDTO = convertToProjectDTO(project);

        FinancialDTO financialDTO = new FinancialDTO();
        financialDTO.setReportDate(new Date()); 
        //financialDTO.setReportPeriod(startDate.toString() + " - " + endDate.toString());
        String reportPeriod = formatDate(startDate) + " - " + formatDate(endDate);
        financialDTO.setReportPeriod(reportPeriod);
        String reportType = determineReportType(startDate, endDate);
        financialDTO.setReportType(reportType);
        financialDTO.setInvoices(invoiceDTOs);
        financialDTO.setBudgetAllocations(budgetAllocationDTOs);
        financialDTO.setProject(projectDTO);

      
        calculateFinancialMetrics(financialDTO);

        FinancialReport financial = convertToFinancialEntity(financialDTO, project); 
        FinancialReport savedFinancialReport = financialRepository.save(financial);

       
        financialDTO.setFReportId(savedFinancialReport.getFReportId());

        return financialDTO;
    }
    private String formatDate(Date date) {
      
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(date);
    }

    private String determineReportType(Date startDate, Date endDate) {
        LocalDate start = convertToLocalDate(startDate);
        LocalDate end = convertToLocalDate(endDate);

        // Calculate the difference in days
        long daysBetween = ChronoUnit.DAYS.between(start, end);

        // Determine the report type based on the difference in days
        if (daysBetween == 0) {
            return "Daily";
        } else if (daysBetween <= 7) {
            return "Semaine";  // Weekly report
        } else if (start.getMonth() == end.getMonth() && start.getYear() == end.getYear()) {
            return "Monthly";  // Monthly report
        } else {
            return "Custom";  // Other custom period
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
    private FinancialReport convertToFinancialEntity(FinancialDTO financialDTO, Project project) {
        if (financialDTO == null) {
            return null;
        }

        FinancialReport financial = new FinancialReport();
        financial.setReportDate(financialDTO.getReportDate());
        financial.setReportPeriod(financialDTO.getReportPeriod());
        financial.setReportType(financialDTO.getReportType());
        financial.setTotalIncome(financialDTO.getTotalIncome());
        financial.setTotalExpenditure(financialDTO.getTotalExpenditure());
        financial.setNetIncome(financialDTO.getNetIncome());

        financial.setProject(project);

        return financial;
    }

    private void calculateFinancialMetrics(FinancialDTO financialDTO) {
        List<InvoiceDTO> invoices = financialDTO.getInvoices();
        List<BudgetAllocationDTO> budgetAllocations = financialDTO.getBudgetAllocations();

        double totalIncome = invoices.stream()
                                     .mapToDouble(InvoiceDTO::getTotalAmount)
                                     .sum();

       
        double totalExpenditure = budgetAllocations.stream()
                                                   .mapToDouble(BudgetAllocationDTO::getAllocatedAmount)
                                                   .sum();

        double netIncome = totalExpenditure - totalIncome ;

        financialDTO.setTotalIncome(totalIncome);
        financialDTO.setTotalExpenditure(totalExpenditure);
        financialDTO.setNetIncome(netIncome);

        logger.info("Total Income: {}", totalIncome);
        logger.info("Total Expenditure: {}", totalExpenditure);
        logger.info("Net Income: {}", netIncome);
    }

    private InvoiceDTO convertToInvoiceDTO(Invoice invoice) {
        List<PurchaseOrderDTO> purchaseOrderDTOs = invoice.getPurchaseOrders() != null 
            ? invoice.getPurchaseOrders().stream()
                .map(po -> new PurchaseOrderDTO(
                    po.getPurchaseId(),
                    po.getOrderNumber(),
                    po.getQuantity(),
                    po.getPurchaseName(),
                    po.getDescription(),
                    po.getUnitPrice(),
                    po.getTotalAmount(),
                    po.getPurchaseDate(),
                    po.getExpectedDeliveryDate(), 
                    po.getStatus(),
                    po.getSupplierId(),
                    po.getProjectId(),
                    po.getInvoiceId()
                ))
                .collect(Collectors.toList())
            : new ArrayList<>();

        return new InvoiceDTO(
            invoice.getInvoiceId(),
            invoice.getInvoiceNumber(),
            invoice.getTotalAmount(),
            invoice.getInvoiceDate(),
            invoice.getDueDate(),
            invoice.isPaid(),
            invoice.getDescription(),
            invoice.getStatus() != null ? invoice.getStatus().name() : null, // Enum to String conversion
            invoice.getBudget() != null ? invoice.getBudget().getBudgetId() : null,
            invoice.getFinancialReport() != null ? invoice.getFinancialReport().getFReportId() : null,
            invoice.getProject() != null ? invoice.getProject().getProjectId() : null,
            purchaseOrderDTOs 
        );
    }

    private BudgetAllocationDTO convertToBudgetAllocationDTO(BudgetAllocation budgetAllocation) {
        if (budgetAllocation == null) {
            return null;
        }

        return new BudgetAllocationDTO(
            budgetAllocation.getBudgetNumber(),
            budgetAllocation.getAllocatedAmount(),
            budgetAllocation.getUsedBudget(),
            budgetAllocation.getRemainingBudget(),
            budgetAllocation.getStartDate(),
            budgetAllocation.getEndDate(),
            budgetAllocation.getBudgetStatus(),
            budgetAllocation.getCategory() 
        );
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        if (project == null) {
            return null;
        }

        Long departmentId = project.getDepartment() != null ? project.getDepartment().getDepartmentId() : null;
        Long solidarityOlympicId = project.getSolidarityOlympic() != null ? project.getSolidarityOlympic().getSolidarityOlympicId() : null;

        return new ProjectDTO(
            project.getProjectId(),
            project.getProjectName(),
            project.getDescription(),
            project.getStartDate(), 
            project.getEndDate(), 
            project.getValidationDate(), 
            project.getStatus(), 
            project.getTechnicalReportSubmitted(),
            project.getFinancialReportSubmitted(),
            project.getIncludedInProgram(),
            project.getReportSubmissionDate(), 
            departmentId,
            solidarityOlympicId 
        );
    }




    
    
    
   /* private void calculateFinancialMetrics(FinancialReport financialReport) {
        
        double totalInvoiceAmount = financialReport.getInvoices().stream()
                .mapToDouble(Invoice::getAmount)
                .sum();
        financialReport.setTotalInvoiceAmount(totalInvoiceAmount);

       
        double totalBudgetAllocationAmount = financialReport.getBudgets().stream()
                .mapToDouble(BudgetAllocation::getAmountAllocated)
                .sum();
        financialReport.setTotalBudgetAllocationAmount(totalBudgetAllocationAmount);

      
    }
    */
    
    
}
