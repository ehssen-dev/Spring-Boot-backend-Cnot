package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;

public interface IPerformanceMetricsService {
	
	void createPerformanceMetrics(PerformanceMetrics performanceMetrics);
	
	void updatePerformanceMetrics(PerformanceMetrics performanceMetrics);

    PerformanceMetrics getPerformanceMetricsByAthlete(Long athleteId);

	void generatePerformanceMetricsForAthlete(Athlete athlete);

    

	
}
