package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.enums.RequestStatus;

@Repository
public interface ProcurementRequestRepository extends JpaRepository<ProcurementRequest, Long> {
	 List<ProcurementRequest> findByRequestedBy(String requestedBy);
	    List<ProcurementRequest> findByStatus(String status);
	    List<ProcurementRequest> findByRequestedDateBetween(LocalDate startDate, LocalDate endDate);
	    List<ProcurementRequest> findByStatus(RequestStatus status);
	}
