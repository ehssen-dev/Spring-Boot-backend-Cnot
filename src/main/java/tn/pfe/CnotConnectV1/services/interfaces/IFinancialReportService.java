package tn.pfe.CnotConnectV1.services.interfaces;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.FinancialDTO;
import tn.pfe.CnotConnectV1.entities.FinancialReport;

public interface IFinancialReportService {

	FinancialDTO generateFinancialReportForPeriod(Date startDate, Date endDate, Long projectId);


	List<FinancialReport> findAll();

	Optional<FinancialReport> findById(Long fReportId);

	List<FinancialReport> getFinancialReportsByProjectId(Long projectId);


	Map<String, Double> getFinancialReportChartData(Long projectId, Date startDate, Date endDate);




}
