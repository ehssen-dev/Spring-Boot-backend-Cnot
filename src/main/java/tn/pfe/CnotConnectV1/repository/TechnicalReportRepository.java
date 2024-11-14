package tn.pfe.CnotConnectV1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.dto.TechnicalReportDTO;
import tn.pfe.CnotConnectV1.entities.TechnicalReport;

@Repository
public interface TechnicalReportRepository extends JpaRepository<TechnicalReport, Long> {
	@Query("SELECT tr FROM TechnicalReport tr WHERE tr.project.id = :projectId")
    TechnicalReport findByProjectId(@Param("projectId") Long projectId);
	
}