package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.FinancialReport;

@Repository
public interface FinancialReportRepository extends JpaRepository<FinancialReport,Long> {
	List<FinancialReport> findByReportPeriod(String reportPeriod);
    List<FinancialReport> findByReportType(String reportType);
    List<FinancialReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<FinancialReport> findByProject_ProjectId(Long projectId);

}


