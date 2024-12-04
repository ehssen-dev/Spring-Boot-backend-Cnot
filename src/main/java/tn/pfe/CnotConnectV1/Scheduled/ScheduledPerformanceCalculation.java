package tn.pfe.CnotConnectV1.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.services.PerformanceMetricsService;

@Component
public class ScheduledPerformanceCalculation {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PerformanceMetricsService performanceMetricsService;

    @Scheduled(cron = "0 0 2 * * ?") // Runs daily at 2 AM
    public void calculateMetricsForCompletedGames() {
        // Fetch games completed in the last 24 hours
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Game> completedGames = gameRepository.findByEndGameBetween(yesterday.atStartOfDay(), LocalDateTime.now());

        // Calculate metrics for each game's athletes
        for (Game game : completedGames) {
            performanceMetricsService.calculateAndSaveMetricsAfterGame(game.getGameId());
        }
    }
}
