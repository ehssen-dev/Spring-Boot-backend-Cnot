package tn.pfe.CnotConnectV1.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.TechnicalReportDTO;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.TechnicalReport;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;
import tn.pfe.CnotConnectV1.repository.TechnicalReportRepository;
import tn.pfe.CnotConnectV1.services.interfaces.ITechnicalReportService;

@Service
public class TechnicalReportService implements 	ITechnicalReportService {
    
    @Autowired
    private TechnicalReportRepository technicalReportRepository;
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ProjectRepository projectRepository;

  
    private static final Logger logger = LoggerFactory.getLogger(TechnicalReportService.class);
    @Override
    public TechnicalReport createTechnicalReport(Long projectId, TechnicalReportDTO reportDTO) {
        if (projectId == null || reportDTO == null) {
            throw new IllegalArgumentException("Project ID and Technical Report DTO must not be null");
        }

        // Fetch the project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Map DTO to entity
        TechnicalReport report = new TechnicalReport();
        report.setReportDate(reportDTO.getReportDate());
        report.setReportPeriod(reportDTO.getReportPeriod());
        report.setReportType(reportDTO.getReportType());

        // Set the project for the report
        report.setProject(project);

        // Save the report
        TechnicalReport savedReport = technicalReportRepository.save(report);

        // Add the report to the project's list of reports
        project.getTechnicalReports().add(savedReport); // Add to the list of reports
        projectRepository.save(project); // Save the updated project

        // Log the successful creation
        System.out.println("Created Technical Report with ID: " + savedReport.getTReportId() + " for Project ID: " + projectId);

        return savedReport;
    }

    @Override
    public TechnicalReport getTechnicalReportByProjectId(Long projectId) {
        // Fetch the project to ensure it exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Fetch the technical report associated with the project
        return technicalReportRepository.findByProjectId(projectId);
    }
    @Override
    public List<TechnicalReport> findAll() {
        return technicalReportRepository.findAll();
    }

    @Override
    public Optional<TechnicalReport> findById(Long tReportId) {
        return technicalReportRepository.findById(tReportId);
    }

    @Override
    public TechnicalReportDTO updateTechnicalReport(Long reportId, TechnicalReportDTO reportUpdateDTO) {
        TechnicalReport report = technicalReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + reportId));

        // Update fields
        report.setReportDate(reportUpdateDTO.getReportDate());
        report.setReportPeriod(reportUpdateDTO.getReportPeriod());
        report.setReportType(reportUpdateDTO.getReportType());

        // Save and convert to DTO
        TechnicalReport savedReport = technicalReportRepository.save(report);
        return convertToDTO(savedReport);  
    }
    private TechnicalReportDTO convertToDTO(TechnicalReport report) {
        TechnicalReportDTO dto = new TechnicalReportDTO();
        dto.setTReportId(report.getTReportId());
        dto.setReportDate(report.getReportDate());
        dto.setReportPeriod(report.getReportPeriod());
        dto.setReportType(report.getReportType());
        
        return dto;
    }

    @Override
    public void deleteById(Long tReportId) {
        technicalReportRepository.deleteById(tReportId);
    }
}