package tn.pfe.CnotConnectV1.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.SupportRequest;
@Repository
public interface SupportRequestRepository  extends JpaRepository<SupportRequest, Long>{

	List<SupportRequest> findByStatus(String status);

	List<SupportRequest> findByAthlete_AthleteId(Long athleteId);
}
