package tn.pfe.CnotConnectV1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;
import tn.pfe.CnotConnectV1.repository.SolidarityOlympicRepository;
import tn.pfe.CnotConnectV1.services.interfaces.ISolidariteOlympiqueService;

@Service
public class SolidariteOlympiqueService  implements ISolidariteOlympiqueService{
	 @Autowired
	    private SolidarityOlympicRepository solidarityOlympicRepository;
	 @Autowired
	    private ProjectRepository  projectRepository;
	 @Override
	    public SolidarityOlympic addProjectToSolidarityOlympic(Long solidarityOlympicId, Project project) {
	        SolidarityOlympic solidarityOlympic = solidarityOlympicRepository.findById(solidarityOlympicId)
	                .orElseThrow(() -> new ResourceNotFoundException("SolidarityOlympic not found with id: " + solidarityOlympicId));
	        solidarityOlympic.addProject(project);
	        return solidarityOlympicRepository.save(solidarityOlympic);
	    }

	    /**
	     * Create a new Solidarité Olympique program
	     * 
	     * @param program the program to create
	     * @return the created program
	     */
	 @Override
	    public SolidarityOlympic createSolidariteOlympique(SolidarityOlympic program) {
	        return solidarityOlympicRepository.save(program);
	    }
	    /**
	     * Get a Solidarité Olympique program by ID
	     * 
	     * @param id the ID of the program
	     * @return the program
	     */
	 @Override
	    public SolidarityOlympic getSolidariteOlympiqueProgram(Long solidarityOlympicId) {
	        return solidarityOlympicRepository.findById(solidarityOlympicId).orElseThrow(() -> new RuntimeException("Program not found"));
	    }
	    /**
	     * Get all Solidarité Olympique programs
	     * 
	     * @return a list of programs
	     */
	 @Override
	    public List<SolidarityOlympic> getAllSolidariteOlympiquePrograms() {
	        return solidarityOlympicRepository.findAll();
	    }
	    
	    /**
	     * Add a project to a Solidarité Olympique program
	     * 
	     * @param programId the ID of the program
	     * @param projectId the ID of the project
	     */
	 @Override
	    public void addProjectToSolidariteOlympiqueProgram(Long solidarityOlympicId, Long projectId) {
	    	SolidarityOlympic program = getSolidariteOlympiqueProgram(solidarityOlympicId);
	        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
	        program.getProjects().add(project);
	        solidarityOlympicRepository.save(program);
	    }
	    
	    /**
	     * Remove a project from a Solidarité Olympique program
	     * 
	     * @param programId the ID of the program
	     * @param projectId the ID of the project
	     */
	 @Override
	    public void removeProjectFromSolidariteOlympiqueProgram(Long solidarityOlympicId, Long projectId) {
	    	SolidarityOlympic program = getSolidariteOlympiqueProgram(solidarityOlympicId);
	        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
	        program.getProjects().remove(project);
	        solidarityOlympicRepository.save(program);
	    }
	    
	    /**
	     * Get all projects associated with a Solidarité Olympique program
	     * 
	     * @param programId the ID of the program
	     * @return a list of projects
	     */
	 @Override
	    public List<Project> getProjectsBySolidariteOlympiqueProgram(Long solidarityOlympicId) {
	    	SolidarityOlympic program = getSolidariteOlympiqueProgram(solidarityOlympicId);
	        return program.getProjects();
	    }
	    
}
