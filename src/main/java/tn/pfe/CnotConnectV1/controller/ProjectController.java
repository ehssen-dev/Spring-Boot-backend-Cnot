package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.DepartmentDTO;
import tn.pfe.CnotConnectV1.dto.ProjectDTO;
import tn.pfe.CnotConnectV1.dto.SolidarityOlympicDTO;
import tn.pfe.CnotConnectV1.entities.Archive;
import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.enums.ProjectStatus;
import tn.pfe.CnotConnectV1.services.interfaces.IProjectService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000") 
public class ProjectController {

    private final IProjectService projectService;

    @Autowired
    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        // Call the service method which handles DTO
        ProjectDTO createdProjectDTO = projectService.saveProject(projectDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectDTO);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Project> createProjects(@RequestBody ProjectDTO projectDTO) {
        Project createdProject = projectService.addProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }
    
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable("projectId") Long projectId) {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok().body(project);
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody ProjectDTO projectDTO) {
        Project updatedProject = projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.ok().body(updatedProject);
    }
    @PutMapping("/{projectId}/associate-department/{departmentId}")
    public ResponseEntity<ProjectDTO> associateProjectWithDepartment(
        @PathVariable Long projectId,
        @PathVariable Long departmentId
    ) {
        ProjectDTO updatedProjectDTO = projectService.associateProjectWithDepartment(projectId, departmentId);
        return ResponseEntity.ok(updatedProjectDTO);
    }
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectId") Long projectId) {
        projectService.deleteProject(projectId);
        String message = "Project with ID " + projectId + " has been deleted ";
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/all")
    
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByStatus(@PathVariable("status") ProjectStatus status) {
        List<Project> projects = projectService.getProjectsByStatus(status);
        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Project>> getProjectsInDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Project> projects = projectService.getProjectsInDateRange(start, end);
        return ResponseEntity.ok().body(projects);
    }

    @PostMapping("/{projectId}/associate/{solidarityOlympicId}")
    public ResponseEntity<String> associateProjectWithSolidarityOlympic(
            @PathVariable("projectId") Long projectId,
            @PathVariable("solidarityOlympicId") Long solidarityOlympicId) {
        projectService.associateProjectWithSolidarityOlympic(projectId, solidarityOlympicId);
        String message = "Project with ID " + projectId + " successfully associated with Solidarity Olympic Program ID " + solidarityOlympicId;
        return ResponseEntity.ok().body(message);
    }

    
    
    
    
    
    @GetMapping("/stats/project-count-by-status")
    public ResponseEntity<Map<ProjectStatus, Long>> getProjectCountByStatus() {
        Map<ProjectStatus, Long> stats = projectService.getProjectCountByStatus();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/total-budget-allocation")
    public ResponseEntity<Map<String, Double>> getTotalBudgetAllocationByProject() {
        Map<String, Double> budgetAllocations = projectService.getTotalBudgetAllocationByProject();
        return ResponseEntity.ok(budgetAllocations);
    }

    @GetMapping("/stats/total-expenditure-by-department")
    public ResponseEntity<Map<String, Double>> getTotalExpenditureByDepartment() {
        Map<String, Double> expenditureByDepartment = projectService.getTotalExpenditureByDepartment();
        return ResponseEntity.ok(expenditureByDepartment);
    }

    @GetMapping("/stats/projects-by-date-range")
    public ResponseEntity<List<ProjectDTO>> getProjectsByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<ProjectDTO> projects = projectService.getProjectsByDateRange(start, end);
        return ResponseEntity.ok(projects);
    }
    
    
    
    
    
    
    
    
    
    
    
    
}