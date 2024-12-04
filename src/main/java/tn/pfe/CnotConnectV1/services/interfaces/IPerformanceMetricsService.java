package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;

public interface IPerformanceMetricsService {
	
	void createPerformanceMetrics(PerformanceMetrics performanceMetrics);
	
	void updatePerformanceMetrics(PerformanceMetrics performanceMetrics);

    PerformanceMetrics getPerformanceMetricsByAthlete(Long athleteId);

	void generatePerformanceMetricsForAthlete(Athlete athlete);

	void calculateAndSaveMetricsAfterGame(Long gameId);

	/**
	 * Fetch all performance metrics for a specific athlete.
	 * 
	 * @param athleteId ID of the athlete
	 * @return List of performance metrics
	 * @throws IllegalArgumentException if the athlete is not found
	 */
	List<PerformanceMetrics> getMetricsByAthlete(Long athleteId);

	/**
	 * Fetch all performance metrics.
	 * 
	 * @return List of all PerformanceMetrics
	 */
	List<PerformanceMetrics> getAllMetrics();

    

	
}
