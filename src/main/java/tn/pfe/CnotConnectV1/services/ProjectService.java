package tn.pfe.CnotConnectV1.services;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.BudgetAllocationDTO;
import tn.pfe.CnotConnectV1.dto.DepartmentDTO;
import tn.pfe.CnotConnectV1.dto.ProjectDTO;
import tn.pfe.CnotConnectV1.dto.PurchaseOrderDTO;
import tn.pfe.CnotConnectV1.dto.SolidarityOlympicDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.enums.ProjectStatus;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.DepartmentRepository;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;
import tn.pfe.CnotConnectV1.repository.SolidarityOlympicRepository;
import tn.pfe.CnotConnectV1.repository.UserRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IProjectService;

@Service
public class ProjectService implements IProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SolidarityOlympicRepository solidariteOlympiqueProgramRepository;
    
    @Override
    public void associateProjectWithSolidarityOlympic(Long projectId, Long solidarityOlympicId) {
        Project project = projectRepository.findById(projectId)
               .orElseThrow(() -> new RuntimeException("Project not found"));
        SolidarityOlympic solidarityOlympic = solidariteOlympiqueProgramRepository.findById(solidarityOlympicId)
               .orElseThrow(() -> new RuntimeException("SolidarityOlympic not found"));
        project.setSolidarityOlympic(solidarityOlympic);
        projectRepository.save(project);
    }
    
    @Override
    public Project assignDepartmentToProject(Long projectId, Department department) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setDepartment(department);
        return projectRepository.save(project);
    }
    
    @Override
    public Map<ProjectStatus, Long> getProjectCountByStatus() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                       .collect(Collectors.groupingBy(Project::getStatus, Collectors.counting()));
    }

    /**
     * Fetches the total budget allocation for each project.
     */
    @Override
    public Map<String, Double> getTotalBudgetAllocationByProject() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                       .collect(Collectors.toMap(
                           Project::getProjectName,
                           project -> project.getBudgetAllocations().stream()
                                             .mapToDouble(BudgetAllocation::getAllocatedAmount)
                                             .sum()
                       ));
    }

    /**
     * Fetches the total expenditure by department.
     */
    @Override
    public Map<String, Double> getTotalExpenditureByDepartment() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                       .filter(project -> project.getDepartment() != null)
                       .collect(Collectors.groupingBy(
                           project -> project.getDepartment().getName(),
                           Collectors.summingDouble(project -> project.getBudgetAllocations().stream()
                                                                     .mapToDouble(BudgetAllocation::getAllocatedAmount)
                                                                     .sum())
                       ));
    }

    /**
     * Fetches the total expenditure of projects over a date range.
     */
    @Override
    public List<ProjectDTO> getProjectsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Project> projects = projectRepository.findByStartDateBetween(startDate, endDate);
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<Project> findByIncludedInProgramAndSolidarityOlympicId(Boolean includedInProgram,  SolidarityOlympic solidarityOlympic) {
        return projectRepository.findByIncludedInProgramAndSolidarityOlympic(includedInProgram, solidarityOlympic);
    }
    
    @Override
    public Project addProject(ProjectDTO projectDTO) {
        if (projectDTO.getProjectName() == null || projectDTO.getProjectName().isEmpty()) {
            throw new IllegalArgumentException("Project name is required.");
        }

        Department department = departmentRepository.findById(projectDTO.getDepartmentId())
                .orElse(null);
        SolidarityOlympic solidarityOlympic = solidariteOlympiqueProgramRepository.findById(projectDTO.getSolidarityOlympicId())
                .orElse(null);
        User requestedBy = userRepository.findById(projectDTO.getRequestedById())
                .orElse(null);

        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setValidationDate(projectDTO.getValidationDate());
        project.setStatus(projectDTO.getStatus());
        project.setTechnicalReportSubmitted(projectDTO.getTechnicalReportSubmitted());
        project.setFinancialReportSubmitted(projectDTO.getFinancialReportSubmitted());
        project.setIncludedInProgram(projectDTO.getIncludedInProgram());
        project.setReportSubmissionDate(projectDTO.getReportSubmissionDate());
        project.setDepartment(department);
        project.setSolidarityOlympic(solidarityOlympic);
       
        // project.setRequestedBy(requestedBy);

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public ProjectDTO saveProject(ProjectDTO dto) {
        Project project = toEntity(dto);
        
        if (project.getProjectName() == null || project.getProjectName().isEmpty()) {
            throw new IllegalArgumentException("Project name is required.");
        }
        
        Project savedProject = projectRepository.save(project);
        return toDTO(savedProject);
    }

    @Override
    public Project updateProject(Long projectId, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        if (projectDTO.getProjectName() == null || projectDTO.getProjectName().isEmpty()) {
            throw new IllegalArgumentException("Project name is required.");
        }

        Department department = departmentRepository.findById(projectDTO.getDepartmentId())
                .orElse(null);
        SolidarityOlympic solidarityOlympic = solidariteOlympiqueProgramRepository.findById(projectDTO.getSolidarityOlympicId())
                .orElse(null);
        User requestedBy = userRepository.findById(projectDTO.getRequestedById())
                .orElse(null);

        existingProject.setProjectName(projectDTO.getProjectName());
        existingProject.setDescription(projectDTO.getDescription());
        existingProject.setStartDate(projectDTO.getStartDate());
        existingProject.setEndDate(projectDTO.getEndDate());
        existingProject.setValidationDate(projectDTO.getValidationDate());
        existingProject.setStatus(projectDTO.getStatus());
        existingProject.setTechnicalReportSubmitted(projectDTO.getTechnicalReportSubmitted());
        existingProject.setFinancialReportSubmitted(projectDTO.getFinancialReportSubmitted());
        existingProject.setIncludedInProgram(projectDTO.getIncludedInProgram());
        existingProject.setReportSubmissionDate(projectDTO.getReportSubmissionDate());
        existingProject.setDepartment(department);
        existingProject.setSolidarityOlympic(solidarityOlympic);
       
        // existingProject.setRequestedBy(requestedBy);

        return projectRepository.save(existingProject);
    }
    
    @Override
    public ProjectDTO associateProjectWithDepartment(Long projectId, Long departmentId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));

        project.setDepartment(department);

        Project updatedProject = projectRepository.save(project);

        return toDTO(updatedProject);
    }
    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public Project getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        project.getBudgetAllocations().size(); 
        
        return project;
    }
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public ProjectDTO getProjectWithDetails(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        ProjectDTO projectDTO = toDTO(project);

        if (project.getBudgetAllocations() != null) {
            List<BudgetAllocationDTO> budgetAllocationDTOs = project.getBudgetAllocations().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            projectDTO.setBudgetAllocations(budgetAllocationDTOs);
        }

        if (project.getPurchaseOrders() != null) {
            List<PurchaseOrderDTO> purchaseOrderDTOs = project.getPurchaseOrders().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            projectDTO.setPurchaseOrders(purchaseOrderDTOs);
        }

        return projectDTO;
    }

   

    @Override
    public Long getCurrentProjectId() {
        return null;
    }
    @Override
    public List<Project> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }
    @Override
    public List<Project> getProjectsInDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start date and end date are required.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        return projectRepository.findByStartDateBetween(startDate, endDate);
    }
    
   /* public List<Project> getProjectsByProjectManager(Employee projectManager) {
        if (projectManager != null) {
            return projectRepository.findByProjectManager(projectManager);
        } else {
            throw new ProjectManagerNotFoundException("Project manager not found.");
        }
    }
*/
    
    
    
    private Project toEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setValidationDate(dto.getValidationDate());
        project.setStatus(dto.getStatus());
        project.setTechnicalReportSubmitted(dto.getTechnicalReportSubmitted());
        project.setFinancialReportSubmitted(dto.getFinancialReportSubmitted());
        project.setIncludedInProgram(dto.getIncludedInProgram());
        project.setReportSubmissionDate(dto.getReportSubmissionDate());
        
        if (dto.getDepartment() != null) {
            Department department = new Department();
            department.setDepartmentId(dto.getDepartment().getDepartmentId());
            project.setDepartment(department);
        }
        
        if (dto.getSolidarityOlympic() != null) {
            SolidarityOlympic solidarityOlympic = new SolidarityOlympic();
            solidarityOlympic.setSolidarityOlympicId(dto.getSolidarityOlympic().getSolidarityOlympicId());
            project.setSolidarityOlympic(solidarityOlympic);
        }
        
       
        
        return project;
    }

    // Utility method to convert entity to DTO
    private ProjectDTO toDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setValidationDate(project.getValidationDate());
        dto.setStatus(project.getStatus());
        dto.setTechnicalReportSubmitted(project.getTechnicalReportSubmitted());
        dto.setFinancialReportSubmitted(project.getFinancialReportSubmitted());
        dto.setIncludedInProgram(project.getIncludedInProgram());
        dto.setReportSubmissionDate(project.getReportSubmissionDate());
        
        if (project.getDepartment() != null) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setDepartmentId(project.getDepartment().getDepartmentId());
            dto.setDepartment(departmentDTO);
        }
        
        if (project.getSolidarityOlympic() != null) {
            SolidarityOlympicDTO solidarityOlympicDTO = new SolidarityOlympicDTO();
            solidarityOlympicDTO.setSolidarityOlympicId(project.getSolidarityOlympic().getSolidarityOlympicId());
            dto.setSolidarityOlympic(solidarityOlympicDTO);
        }
        
        List<BudgetAllocationDTO> budgetAllocationsDTO = project.getBudgetAllocations().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        dto.setBudgetAllocations(budgetAllocationsDTO);

        List<PurchaseOrderDTO> purchaseOrdersDTO = project.getPurchaseOrders().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        dto.setPurchaseOrders(purchaseOrdersDTO);
        
        return dto; 
    
    
    }
    
 // Conversion method for BudgetAllocation
    private BudgetAllocationDTO toDTO(BudgetAllocation budgetAllocation) {
        BudgetAllocationDTO dto = new BudgetAllocationDTO();
        dto.setBudgetId(budgetAllocation.getBudgetId());
        dto.setBudgetNumber(budgetAllocation.getBudgetNumber());
        dto.setAllocatedAmount(budgetAllocation.getAllocatedAmount());
        dto.setUsedBudget(budgetAllocation.getUsedBudget());
        dto.setRemainingBudget(budgetAllocation.getRemainingBudget());
        dto.setStartDate(budgetAllocation.getStartDate());
        dto.setEndDate(budgetAllocation.getEndDate());
        dto.setBudgetStatus(budgetAllocation.getBudgetStatus());
        dto.setCategory(budgetAllocation.getCategory());
        
        return dto;
    }

    // Conversion method for PurchaseOrder
    private PurchaseOrderDTO toDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setPurchaseId(purchaseOrder.getPurchaseId());
        dto.setOrderNumber(purchaseOrder.getOrderNumber());
        dto.setQuantity(purchaseOrder.getQuantity());
        dto.setPurchaseName(purchaseOrder.getPurchaseName());
        dto.setDescription(purchaseOrder.getDescription());
        dto.setUnitPrice(purchaseOrder.getUnitPrice());
        dto.setTotalAmount(purchaseOrder.getTotalAmount());
        dto.setPurchaseDate(purchaseOrder.getPurchaseDate());
        dto.setExpectedDeliveryDate(purchaseOrder.getExpectedDeliveryDate());
        dto.setStatus(purchaseOrder.getStatus());
        
        return dto;
    }

    
}