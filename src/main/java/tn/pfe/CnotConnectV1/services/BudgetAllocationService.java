package tn.pfe.CnotConnectV1.services;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.BudgetAllocationDTO;
import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.enums.BudgetStatus;
import tn.pfe.CnotConnectV1.exeptions.BudgetAllocationNotFoundException;
import tn.pfe.CnotConnectV1.exeptions.ProjectNotFoundException;
import tn.pfe.CnotConnectV1.repository.BudgetAllocationRepository;
import tn.pfe.CnotConnectV1.repository.FinancialReportRepository;
import tn.pfe.CnotConnectV1.repository.ProcurementRequestRepository;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IBudgetAllocationService;
import tn.pfe.CnotConnectV1.services.interfaces.IProcurementRequestService;
import tn.pfe.CnotConnectV1.services.interfaces.IProjectService;

@Service
public class BudgetAllocationService implements IBudgetAllocationService{

    private static final Logger logger = LoggerFactory.getLogger(BudgetAllocationService.class);

	
    private final BudgetAllocationRepository budgetAllocationRepository;
    private final ProjectRepository projectRepository ;
    @Autowired
    private IProjectService projectService;

    @Autowired
    private IProcurementRequestService procurementRequestService;
    
    @Autowired
    private ProcurementRequestRepository procurementRequestRepository;

    @Autowired
    private FinancialReportRepository financialReportRepository;
    
    public BudgetAllocationService(BudgetAllocationRepository budgetAllocationRepository, ProjectRepository projectRepository) {
        this.budgetAllocationRepository = budgetAllocationRepository;
		this.projectRepository = projectRepository;
    }
	
    @Override
    @Transactional
    public BudgetAllocationDTO addBudgetAllocation(BudgetAllocationDTO dto) {
        ProcurementRequest procurementRequest = dto.getRequestId() != null
                ? procurementRequestRepository.findById(dto.getRequestId())
                  .orElseThrow(() -> new IllegalArgumentException("Invalid procurement request ID"))
                : null;

        FinancialReport financialReport = dto.getFinancialReportId() != null
                ? financialReportRepository.findById(dto.getFinancialReportId())
                  .orElseThrow(() -> new IllegalArgumentException("Invalid financial report ID"))
                : null;

        Project project = dto.getProjectId() != null
                ? projectRepository.findById(dto.getProjectId())
                  .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"))
                : null;

        System.out.println("Project ID: " + dto.getProjectId());

        BudgetAllocation budgetAllocation = new BudgetAllocation();
        budgetAllocation.setAllocatedAmount(dto.getAllocatedAmount());
        
        // Set usedBudget to 0
        budgetAllocation.setUsedBudget(0.0);

        // Set remainingBudget to allocatedAmount
        budgetAllocation.setRemainingBudget(dto.getAllocatedAmount());
        
        // Generate a unique budget number
        String generatedBudgetNumber = generateBudgetNumber();
        budgetAllocation.setBudgetNumber(generatedBudgetNumber);
        
        budgetAllocation.setStartDate(dto.getStartDate());
        budgetAllocation.setEndDate(dto.getEndDate());
        budgetAllocation.setBudgetStatus(dto.getBudgetStatus());
        budgetAllocation.setCategory(dto.getCategory());
        budgetAllocation.setProcurementRequest(procurementRequest);
        budgetAllocation.setFinancialReport(financialReport);
        budgetAllocation.setProject(project);

        BudgetAllocation savedBudgetAllocation = budgetAllocationRepository.save(budgetAllocation);

        return toDTO(savedBudgetAllocation);
    }

    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 7; 

    private String generateBudgetNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);
        
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return sb.insert(LENGTH / 2, '-').toString();
    }

	
	@Override
	    public void updateBudgetStatus(BudgetAllocation budgetAllocation, BudgetStatus newStatus) {
	        budgetAllocation.setBudgetStatus(newStatus);
	        budgetAllocationRepository.save(budgetAllocation);
	    }
	
	@Override
	@Transactional
	public List<BudgetAllocationDTO> getAllBudgetAllocations() {
	    List<BudgetAllocation> budgetAllocations = budgetAllocationRepository.findAll();
	    budgetAllocations.forEach(allocation -> 
        System.out.println("Debug - Budget Number: " + allocation.getBudgetNumber())
    );
	    return budgetAllocations.stream()
	            .map(this::toDTO)
	            .collect(Collectors.toList());
	}
	
	
	 
	 @Override
	 @Transactional
	 public BudgetAllocation allocateBudgetB(BudgetAllocationDTO dto) {
	     // Fetch existing BudgetAllocation
	     BudgetAllocation budgetAllocation = budgetAllocationRepository.findById(dto.getBudgetId())
	             .orElseThrow(() -> new IllegalArgumentException("Budget allocation not found with id: " + dto.getBudgetId()));

	     // Fetch associated ProcurementRequest and Project
	     ProcurementRequest procurementRequest = procurementRequestService.findProcurementRequestById(dto.getRequestId());
	     Project project = projectService.getProjectById(dto.getProjectId());

	     // Set the project in the procurement request if not already set
	     if (procurementRequest.getProject() == null) {
	         procurementRequest.setProject(project);
	     }

	     // Set the ProcurementRequest and Project in BudgetAllocation
	     budgetAllocation.setProcurementRequest(procurementRequest);
	     budgetAllocation.setProject(project);

	     // Update and save the BudgetAllocation
	     return budgetAllocationRepository.save(budgetAllocation);
	 }

	 @Override
	 @Transactional
	 public BudgetAllocation allocateBudget(Long budgetId, BudgetAllocationDTO dto) {
	     // Fetch existing BudgetAllocation
	     BudgetAllocation budgetAllocation = budgetAllocationRepository.findById(budgetId)
	             .orElseThrow(() -> new IllegalArgumentException("Budget allocation not found with id: " + budgetId));

	     // Fetch associated ProcurementRequest and Project
	     ProcurementRequest procurementRequest = procurementRequestService.findProcurementRequestById(dto.getRequestId());
	     Project project = projectService.getProjectById(dto.getProjectId());

	     // Set the project in the procurement request if not already set
	     if (procurementRequest.getProject() == null) {
	         procurementRequest.setProject(project);
	     }

	     // Set the ProcurementRequest and Project in BudgetAllocation
	     budgetAllocation.setProcurementRequest(procurementRequest);
	     budgetAllocation.setProject(project);

	     // Update and save the BudgetAllocation
	     return budgetAllocationRepository.save(budgetAllocation);
	 }

	 
	 
	 @Override
	 @Transactional
	 public BudgetAllocationDTO updateBudgetAllocation(Long budgetId, BudgetAllocationDTO dto) {
	     // Fetch the existing BudgetAllocation
	     BudgetAllocation existingBudgetAllocation = getBudgetAllocationById(budgetId);
	     
	     // Fetch related entities if IDs are provided in the DTO
	     ProcurementRequest procurementRequest = dto.getRequestId() != null
	             ? procurementRequestRepository.findById(dto.getRequestId())
	               .orElseThrow(() -> new IllegalArgumentException("Invalid procurement request ID"))
	             : existingBudgetAllocation.getProcurementRequest();

	     FinancialReport financialReport = dto.getFinancialReportId() != null
	             ? financialReportRepository.findById(dto.getFinancialReportId())
	               .orElseThrow(() -> new IllegalArgumentException("Invalid financial report ID"))
	             : existingBudgetAllocation.getFinancialReport();

	     Project project = dto.getProjectId() != null
	             ? projectRepository.findById(dto.getProjectId())
	               .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"))
	             : existingBudgetAllocation.getProject();

	     // Update fields
	     existingBudgetAllocation.setAllocatedAmount(dto.getAllocatedAmount());
	     existingBudgetAllocation.setUsedBudget(dto.getUsedBudget());
	     existingBudgetAllocation.setRemainingBudget(dto.getRemainingBudget());
	     existingBudgetAllocation.setStartDate(dto.getStartDate());
	     existingBudgetAllocation.setEndDate(dto.getEndDate());
	     existingBudgetAllocation.setBudgetStatus(dto.getBudgetStatus());
	     existingBudgetAllocation.setCategory(dto.getCategory());
	     existingBudgetAllocation.setProcurementRequest(procurementRequest);
	     existingBudgetAllocation.setFinancialReport(financialReport);
	     existingBudgetAllocation.setProject(project);

	     // Save the updated entity
	     BudgetAllocation updatedBudgetAllocation = budgetAllocationRepository.save(existingBudgetAllocation);

	     // Convert to DTO and return
	     return toDTO(updatedBudgetAllocation);
	 }
	@Override
	    public void deleteBudgetAllocation(Long budgetId) {
	        BudgetAllocation budgetAllocation = getBudgetAllocationById(budgetId);
	        // Add business logic for validation, authorization, etc.
	        budgetAllocationRepository.delete(budgetAllocation);
	    }
	
	@Override
    @Transactional
    public BudgetAllocation getBudgetAllocationById(Long budgetId) {
        logger.info("Attempting to retrieve BudgetAllocation with ID: {}", budgetId);
        return budgetAllocationRepository.findById(budgetId)
                .orElseThrow(() -> {
                    logger.error("BudgetAllocation not found with ID: {}", budgetId);
                    return new BudgetAllocationNotFoundException("Budget allocation not found with id: " + budgetId);
                });
    }
	@Override
	    public Double calculateTotalBudgetForProject(Long projectId) {
	        Project project = projectRepository.findById(projectId)
	                              .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
	
	        List<BudgetAllocation> budgetAllocations = budgetAllocationRepository.findByProject(project);
	        
	        Double totalBudget = budgetAllocations.stream()
	                .mapToDouble(BudgetAllocation::getAllocatedAmount)
	                .sum();
	
	        return totalBudget;
	    }
	    
   
	@Transactional
	@Override
	public void addExpenseToBudget(Long budgetId, Double expenseAmount) {
	    // Fetch the budget allocation
	    BudgetAllocation budget = budgetAllocationRepository.findById(budgetId)
	            .orElseThrow(() -> new RuntimeException("Budget not found"));

	    // Validate the expense amount
	    if (expenseAmount < 0) {
	        throw new IllegalArgumentException("Expense amount must be positive");
	    }

	    // Update the used budget
	    Double newUsedBudget = budget.getUsedBudget() + expenseAmount;
	    budget.setUsedBudget(newUsedBudget);

	    // Update the remaining budget
	    Double remainingBudget = budget.getAllocatedAmount() - newUsedBudget;
	    budget.setRemainingBudget(remainingBudget);

	    // Check for budget overrun
	    if (remainingBudget < 0) {
	        budget.setBudgetStatus(BudgetStatus.IN_DANGER);
	    } else if (newUsedBudget > budget.getAllocatedAmount() * 0.9) { // Example threshold for danger status
	        budget.setBudgetStatus(BudgetStatus.IN_DANGER);
	    } else {
	        budget.setBudgetStatus(BudgetStatus.GOOD); // Assuming this is the default status
	    }

	    // Save the updated budget allocation
	    budgetAllocationRepository.save(budget);
	}

	
	
	
	

	
	
	
	private BudgetAllocationDTO toDTO(BudgetAllocation budgetAllocation) {
	    System.out.println("Converting BudgetAllocation to DTO: BudgetNumber=" + budgetAllocation.getBudgetNumber());

        return new BudgetAllocationDTO(
                budgetAllocation.getBudgetId(),
                budgetAllocation.getBudgetNumber(),
                budgetAllocation.getAllocatedAmount(),
                budgetAllocation.getUsedBudget(),
                budgetAllocation.getRemainingBudget(),
                budgetAllocation.getStartDate(),
                budgetAllocation.getEndDate(),
                budgetAllocation.getBudgetStatus(),
                budgetAllocation.getCategory(),
                budgetAllocation.getProcurementRequest() != null ? budgetAllocation.getProcurementRequest().getRequestId() : null,
                budgetAllocation.getFinancialReport() != null ? budgetAllocation.getFinancialReport().getFReportId() : null,
                budgetAllocation.getProject() != null ? budgetAllocation.getProject().getProjectId() : null
        );
    }
    
    
    
    
    

}