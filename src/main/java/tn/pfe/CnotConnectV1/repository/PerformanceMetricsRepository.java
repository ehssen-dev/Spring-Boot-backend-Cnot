package tn.pfe.CnotConnectV1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;

@Repository
public interface PerformanceMetricsRepository  extends JpaRepository<PerformanceMetrics, Long>{

    Optional<PerformanceMetrics> findByAthlete_AthleteId(Long athleteId);

	//List<PerformanceMetrics> findByFederationId(Long federationId);

	//List<PerformanceMetrics> findByAthlete(Athlete athlete);
	Optional<PerformanceMetrics> findByAthlete(Athlete athlete);

}
