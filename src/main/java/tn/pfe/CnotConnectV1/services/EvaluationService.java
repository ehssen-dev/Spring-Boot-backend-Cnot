package tn.pfe.CnotConnectV1.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import tn.pfe.CnotConnectV1.dto.CriteriaDTO;
import tn.pfe.CnotConnectV1.dto.EvaluationReportDTO;
import tn.pfe.CnotConnectV1.dto.KPIReportDTO;
import tn.pfe.CnotConnectV1.dto.PerformanceDataDTO;
import tn.pfe.CnotConnectV1.entities.Criteria;
import tn.pfe.CnotConnectV1.entities.Processes;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.CriteriaRepository;
import tn.pfe.CnotConnectV1.repository.ProcessRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IEvaluationService;

public class EvaluationService  implements IEvaluationService {
	 @Autowired
	    private CriteriaRepository criteriaRepository;

	    @Autowired
	    private ProcessRepository processRepository;
	    @Override
	    public CriteriaDTO createCriteria(CriteriaDTO criteriaDTO) {
	        Criteria criteria = new Criteria();
	        // Populate criteria entity with criteriaDTO data
	        criteria.setName(criteriaDTO.getName());
	        criteria.setDescription(criteriaDTO.getDescription());
	        criteria.setType(criteriaDTO.getType());

	        criteria = criteriaRepository.save(criteria);
	        return new CriteriaDTO(criteria);
	    }

	    @Override
	    public CriteriaDTO updateCriteria(Long criteriaId, CriteriaDTO criteriaDTO) {
	        Criteria criteria = criteriaRepository.findById(criteriaId)
	                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found"));

	        // Update criteria entity with new data from criteriaDTO
	        criteria.setName(criteriaDTO.getName());
	        criteria.setDescription(criteriaDTO.getDescription());
	        criteria.setType(criteriaDTO.getType());

	        criteria = criteriaRepository.save(criteria);
	        return new CriteriaDTO(criteria);
	    }

	    @Override
	    public CriteriaDTO getCriteria(Long criteriaId) {
	        Criteria criteria = criteriaRepository.findById(criteriaId)
	                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found"));
	        return new CriteriaDTO(criteria);
	    }

	    @Override
	    public void deleteCriteria(Long criteriaId) {
	        Criteria criteria = criteriaRepository.findById(criteriaId)
	                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found"));
	        criteriaRepository.delete(criteria);
	    }

	    @Override
	    public List<CriteriaDTO> listAllCriteria() {
	        List<Criteria> criteriaList = criteriaRepository.findAll();
	        return criteriaList.stream().map(CriteriaDTO::new).collect(Collectors.toList());
	    }

	    @Override
	    public void assignCriteriaToProcess(Long processId, Long criteriaId) {
	    	Processes process = processRepository.findById(processId)
	                .orElseThrow(() -> new ResourceNotFoundException("Process not found"));

	        Criteria criteria = criteriaRepository.findById(criteriaId)
	                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found"));

	        // Assuming there is a relationship between Process and Criteria
	        process.addCriteria(criteria);
	        processRepository.save(process);
	    }

	    @Override
	    public void trackPerformance(Long processId, PerformanceDataDTO performanceData) {
	    	Processes process = processRepository.findById(processId)
	                .orElseThrow(() -> new ResourceNotFoundException("Process not found"));

	        // Logic to track performance based on the provided data
	        // This could involve updating metrics, recording results, etc.
	        process.trackPerformance(performanceData);
	        processRepository.save(process);
	    }

	    @Override
	    public EvaluationReportDTO generateEvaluationReport(Long processId, Date startDate, Date endDate) {
	    	Processes process = processRepository.findById(processId)
	                .orElseThrow(() -> new ResourceNotFoundException("Process not found"));

	        // Logic to generate evaluation report based on the process and date range
	        // This could include calculating metrics, summarizing findings, etc.
	        EvaluationReportDTO report = new EvaluationReportDTO();
	        // Populate report with relevant data

	        return report;
	    }

	    @Override
	    public KPIReportDTO generateKPIReport(Long criteriaId, Date startDate, Date endDate) {
	        Criteria criteria = criteriaRepository.findById(criteriaId)
	                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found"));

	        // Logic to generate KPI report based on criteria and date range
	        // This could involve aggregating performance data, calculating KPIs, etc.
	        KPIReportDTO kpiReport = new KPIReportDTO();
	        // Populate KPI report with relevant data

	        return kpiReport;
	    }
	}
