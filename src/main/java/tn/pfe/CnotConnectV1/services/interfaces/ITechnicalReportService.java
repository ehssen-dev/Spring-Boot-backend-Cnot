package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.TechnicalReportDTO;
import tn.pfe.CnotConnectV1.entities.TechnicalReport;

public interface ITechnicalReportService {
	  	List<TechnicalReport> findAll();
	  	
	    Optional<TechnicalReport> findById(Long tReportId);
	   // TechnicalReport save(TechnicalReport technicalReport);
	    
	    void deleteById(Long tReportId);
	//	TechnicalReport submitTechnicalReport(TechnicalReport technicalReport, Long projectId);
	    
		TechnicalReport createTechnicalReport(Long projectId, TechnicalReportDTO reportDTO);
		
		//TechnicalReport updateTechnicalReport(Long reportId, TechnicalReportDTO reportUpdateDTO);
		
		TechnicalReportDTO updateTechnicalReport(Long reportId, TechnicalReportDTO reportUpdateDTO);
		
		TechnicalReport getTechnicalReportByProjectId(Long projectId);
}