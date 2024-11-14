package tn.pfe.CnotConnectV1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;
import tn.pfe.CnotConnectV1.entities.Result;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.repository.PerformanceMetricsRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IPerformanceMetricsService;

@Service
public class PerformanceMetricsService implements IPerformanceMetricsService {

    private final PerformanceMetricsRepository performanceMetricsRepository;
    private final GameRepository gameRepository;

    @Autowired
    public PerformanceMetricsService(PerformanceMetricsRepository performanceMetricsRepository, GameRepository gameRepository) {
        this.performanceMetricsRepository = performanceMetricsRepository;
		this.gameRepository = gameRepository;
    }
    @Override
    public void createPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        performanceMetricsRepository.save(performanceMetrics);
    }
    @Override
    public void updatePerformanceMetrics(PerformanceMetrics performanceMetrics) {
        performanceMetricsRepository.save(performanceMetrics);
    }
    @Override
    public PerformanceMetrics getPerformanceMetricsByAthlete(Long athleteId) {
        Optional<PerformanceMetrics> performanceMetrics = performanceMetricsRepository.findByAthlete_AthleteId(athleteId);
        return performanceMetrics.orElse(null);
    }
   
    @Override
    public void generatePerformanceMetricsForAthlete(Athlete athlete) {
        // Retrieve all games in which the athlete participated (assuming past games)
        List<Game> athleteGames = gameRepository.findByAthletesContains(athlete);

        // Initialize variables to calculate performance metrics
        int goldMedals = 0;
        int silverMedals = 0;
        int bronzeMedals = 0;
        double totalFinishTime = 0;
        int totalGames = athleteGames.size(); // Track total games participated

        // Iterate through the athlete's games to calculate metrics
        for (Game game : athleteGames) {
            Result result = game.getResult();
            if (result != null) {
                // Check the outcome of the game and update medal counts
            	if (result.getWinner().equals(athlete.getFirstName() + " " + athlete.getLastName())) {
            	    goldMedals++;
            	}else if (result.getRunnerUp().equals(athlete.getFirstName() + " " + athlete.getLastName())) {
                    silverMedals++;
                } else if (result.getThirdPlace().equals(athlete.getFirstName() + " " + athlete.getLastName())) {
                    bronzeMedals++;
                }

               
            }
        }

        // Calculate additional metrics (optional)
        double averageFinishTime = (totalFinishTime > 0 && totalGames > 0) ? totalFinishTime / totalGames : 0.0;

        // Create a new PerformanceMetrics object and set calculated values
        PerformanceMetrics performanceMetrics = new PerformanceMetrics();
        performanceMetrics.setAthlete(athlete);
        performanceMetrics.setGoldMedals(goldMedals);
        performanceMetrics.setSilverMedals(silverMedals);
        performanceMetrics.setBronzeMedals(bronzeMedals);
        performanceMetrics.setTotalMedals(goldMedals + silverMedals + bronzeMedals);
        performanceMetrics.setTotalGames(totalGames);
        performanceMetrics.setAverageFinishTime(averageFinishTime);

        // Save the generated performance metrics to the database
        performanceMetricsRepository.save(performanceMetrics);
    }
/*
    // Method to update performance metrics for an athlete after a new game result is recorded
    public void updatePerformanceMetricsAfterGame(Result result) {
        Athlete athlete = result.getAthlete();
        generatePerformanceMetricsForAthlete(athlete);
    }*/
    
    
    
}