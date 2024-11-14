package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.Date;
import java.util.List;

import tn.pfe.CnotConnectV1.dto.CriteriaDTO;
import tn.pfe.CnotConnectV1.dto.EvaluationReportDTO;
import tn.pfe.CnotConnectV1.dto.KPIReportDTO;
import tn.pfe.CnotConnectV1.dto.PerformanceDataDTO;

public interface IEvaluationService {

    // Criteria Management
    CriteriaDTO createCriteria(CriteriaDTO criteriaDTO);
    CriteriaDTO updateCriteria(Long criteriaId, CriteriaDTO criteriaDTO);
    CriteriaDTO getCriteria(Long criteriaId);
    void deleteCriteria(Long criteriaId);
    List<CriteriaDTO> listAllCriteria();

    // Assigning and Tracking Performance
    void assignCriteriaToProcess(Long processId, Long criteriaId);
    void trackPerformance(Long processId, PerformanceDataDTO performanceData);

    // Reporting and Analytics
    EvaluationReportDTO generateEvaluationReport(Long processId, Date startDate, Date endDate);
    KPIReportDTO generateKPIReport(Long criteriaId, Date startDate, Date endDate);
}