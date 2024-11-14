package tn.pfe.CnotConnectV1.services.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import tn.pfe.CnotConnectV1.dto.ProjectDTO;
import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.enums.ProjectStatus;

public interface IProjectService {

    /**
     * Associates a project with a Solidarity Olympic program.
     *
     * @param projectId the ID of the project
     * @param solidarityOlympicId the ID of the Solidarity Olympic program
     * @throws RuntimeException if either project or Solidarity Olympic program is not found
     */
    void associateProjectWithSolidarityOlympic(Long projectId, Long solidarityOlympicId);

	Project assignDepartmentToProject(Long projectId, Department department);

    
    
    
    /**
     * Finds projects based on inclusion in a program and Solidarity Olympic program association.
     *
     * @param includedInProgram whether the project should be included in a program
     * @param solidarityOlympic the Solidarity Olympic program to filter by
     * @return a list of projects matching the criteria
     */
    List<Project> findByIncludedInProgramAndSolidarityOlympicId(Boolean includedInProgram, SolidarityOlympic solidarityOlympic);

    /**
     * Saves a new project.
     *
     * @param project the project to save
     * @return the saved project
     * @throws IllegalArgumentException if project name is null or empty
     */
	Project addProject(ProjectDTO projectDTO);


    /**
     * Updates an existing project.
     *
     * @param projectId the ID of the project to update
     * @param updatedProject the updated project data
     * @return the updated project
     * @throws EntityNotFoundException if the project is not found
     * @throws IllegalArgumentException if updated project name is null or empty
     */
	Project updateProject(Long projectId, ProjectDTO projectDTO);

    /**
     * Deletes a project by ID.
     *
     * @param projectId the ID of the project to delete
     */
    void deleteProject(Long projectId);

    /**
     * Gets a project by ID.
     *
     * @param projectId the ID of the project to retrieve
     * @return the project with the given ID
     * @throws EntityNotFoundException if the project is not found
     */
    Project getProjectById(Long projectId);

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects
     */
   // List<Project> getAllProjects();

    /**
     * (Optional method, not implemented in the provided code)
     * Gets the current project ID (implementation depends on your application logic).
     *
     * @return the current project ID
     */
    Long getCurrentProjectId();

    /**
     * Gets projects by their status.
     *
     * @param status the project status to filter by
     * @return a list of projects with the specified status
     */
    List<Project> getProjectsByStatus(ProjectStatus status);

    /**s
     * Gets projects within a date range.
     *
     * @param startDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @return a list of projects with start dates between the provided range
     * @throws IllegalArgumentException if start date is after end date or either date is null
     */
    List<Project> getProjectsInDateRange(LocalDate startDate, LocalDate endDate);

	ProjectDTO saveProject(ProjectDTO dto);

	ProjectDTO associateProjectWithDepartment(Long projectId, Long departmentId);

	ProjectDTO getProjectWithDetails(Long projectId);

	List<ProjectDTO> getAllProjects();

	Map<ProjectStatus, Long> getProjectCountByStatus();

	/**
	 * Fetches the total budget allocation for each project.
	 */
	Map<String, Double> getTotalBudgetAllocationByProject();

	/**
	 * Fetches the total expenditure by department.
	 */
	Map<String, Double> getTotalExpenditureByDepartment();

	/**
	 * Fetches the total expenditure of projects over a date range.
	 */
	List<ProjectDTO> getProjectsByDateRange(LocalDate startDate, LocalDate endDate);






    /**
     * (Optional method, not implemented in the provided code)
     * Gets projects by the assigned project manager.
     *
     * @param projectManager the project manager to filter by
     * @return a list of projects assigned to the specified project manager
     * @throws ProjectManagerNotFoundException if the project manager is not found
     */
    // List<Project> getProjectsByProjectManager(Employee projectManager);
}