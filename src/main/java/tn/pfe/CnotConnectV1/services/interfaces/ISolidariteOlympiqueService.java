package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;

public interface ISolidariteOlympiqueService {

	/**
	 * Get all projects associated with a Solidarité Olympique program
	 * 
	 * @param programId the ID of the program
	 * @return a list of projects
	 */
	List<Project> getProjectsBySolidariteOlympiqueProgram(Long solidarityOlympicId);

	/**
	 * Remove a project from a Solidarité Olympique program
	 * 
	 * @param programId the ID of the program
	 * @param projectId the ID of the project
	 */
	void removeProjectFromSolidariteOlympiqueProgram(Long solidarityOlympicId, Long projectId);

	/**
	 * Add a project to a Solidarité Olympique program
	 * 
	 * @param programId the ID of the program
	 * @param projectId the ID of the project
	 */
	void addProjectToSolidariteOlympiqueProgram(Long solidarityOlympicId, Long projectId);

	/**
	 * Get all Solidarité Olympique programs
	 * 
	 * @return a list of programs
	 */
	List<SolidarityOlympic> getAllSolidariteOlympiquePrograms();

	/**
	 * Get a Solidarité Olympique program by ID
	 * 
	 * @param id the ID of the program
	 * @return the program
	 */
	SolidarityOlympic getSolidariteOlympiqueProgram(Long solidarityOlympicId);

	/**
	 * Create a new Solidarité Olympique program
	 * 
	 * @param program the program to create
	 * @return the created program
	 */
	SolidarityOlympic createSolidariteOlympique(SolidarityOlympic program);

	SolidarityOlympic addProjectToSolidarityOlympic(Long solidarityOlympicId, Project project);

}
