package tn.pfe.CnotConnectV1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;
import tn.pfe.CnotConnectV1.entities.Result;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.repository.PerformanceMetricsRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IPerformanceMetricsService;

@Service
public class PerformanceMetricsService implements IPerformanceMetricsService {

    private final PerformanceMetricsRepository performanceMetricsRepository;
    private final GameRepository gameRepository;
    private final AthleteRepository athleteRepository;

    
    @Autowired
    public PerformanceMetricsService(PerformanceMetricsRepository performanceMetricsRepository, GameRepository gameRepository, AthleteRepository athleteRepository) {
        this.performanceMetricsRepository = performanceMetricsRepository;
		this.gameRepository = gameRepository;
		this.athleteRepository = athleteRepository;
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
        List<Game> athleteGames = gameRepository.findByAthletesContains(athlete);

        int goldMedals = 0;
        int silverMedals = 0;
        int bronzeMedals = 0;
        double totalFinishTime = 0;
        int totalGames = athleteGames.size(); 

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
        double averageFinishTime = (totalFinishTime > 0 && totalGames > 0) ? totalFinishTime / totalGames : 0.0;

  
        PerformanceMetrics performanceMetrics = new PerformanceMetrics();
        performanceMetrics.setAthlete(athlete);
        performanceMetrics.setGoldMedals(goldMedals);
        performanceMetrics.setSilverMedals(silverMedals);
        performanceMetrics.setBronzeMedals(bronzeMedals);
        performanceMetrics.setTotalMedals(goldMedals + silverMedals + bronzeMedals);
        performanceMetrics.setTotalGames(totalGames);
        performanceMetrics.setAverageFinishTime(averageFinishTime);

        performanceMetricsRepository.save(performanceMetrics);
    }
/*
    // Method to update performance metrics for an athlete after a new game result is recorded
    public void updatePerformanceMetricsAfterGame(Result result) {
        Athlete athlete = result.getAthlete();
        generatePerformanceMetricsForAthlete(athlete);
    }*/
    
    @Transactional
    @Override
    public void calculateAndSaveMetricsAfterGame(Long gameId) {
       
        Game game = gameRepository.findById(gameId)
                                  .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        Result result = game.getResult();
        if (result == null) {
            throw new IllegalArgumentException("Result not found for the game");
        }

        for (Athlete athlete : game.getAthletes()) {
         
            PerformanceMetrics metrics = performanceMetricsRepository.findByAthlete(athlete)
                                                   .orElse(new PerformanceMetrics());

            metrics.setAthlete(athlete);

            if (result.getWinner() != null && result.getWinner().equals(athlete.getFirstName() + " " + athlete.getLastName())) {
                metrics.setGoldMedals(metrics.getGoldMedals() + 1);
            } else if (result.getRunnerUp() != null && result.getRunnerUp().equals(athlete.getFirstName() + " " + athlete.getLastName())) {
                metrics.setSilverMedals(metrics.getSilverMedals() + 1);
            } else if (result.getThirdPlace() != null && result.getThirdPlace().equals(athlete.getFirstName() + " " + athlete.getLastName())) {
                metrics.setBronzeMedals(metrics.getBronzeMedals() + 1);
            }

            metrics.setTotalGames(metrics.getTotalGames() + 1);
            metrics.setTournamentParticipation(metrics.getTournamentParticipation() + 1);

            metrics.setAdherenceToRegulations(metrics.isAdherenceToRegulations() && game.hasEnded());

            double averageFinishTime = 0;
            if (result != null && result.getScores() != null) {
                try {
                    averageFinishTime = Double.parseDouble(result.getScores());
                } catch (NumberFormatException e) {
                    averageFinishTime = 0;
                }
            }
            metrics.setAverageFinishTime(averageFinishTime);

            System.out.println("Processing athlete: " + athlete.getFirstName() + " " + athlete.getLastName());
            System.out.println("Updating PerformanceMetrics for: " + metrics.getAthlete().getFirstName() + " " + metrics.getAthlete().getLastName());
            System.out.println("Medals: " + metrics.getGoldMedals() + " gold, " + metrics.getSilverMedals() + " silver, " + metrics.getBronzeMedals() + " bronze");
            performanceMetricsRepository.save(metrics);

        }
    }

    
    /**
     * Fetch all performance metrics for a specific athlete.
     * 
     * @param athleteId ID of the athlete
     * @return List of performance metrics
     * @throws IllegalArgumentException if the athlete is not found
     */
    @Override
    public List<PerformanceMetrics> getMetricsByAthlete(Long athleteId) {
        Athlete athlete = athleteRepository.findById(athleteId)
                                           .orElseThrow(() -> new IllegalArgumentException("Athlete not found"));
        return athlete.getPerformanceMetrics();
    }
    
    /**
     * Fetch all performance metrics.
     * 
     * @return List of all PerformanceMetrics
     */
    @Override
    public List<PerformanceMetrics> getAllMetrics() {
        return performanceMetricsRepository.findAll();
    }
    
}