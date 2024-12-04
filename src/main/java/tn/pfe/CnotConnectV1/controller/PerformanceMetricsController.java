package tn.pfe.CnotConnectV1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;
import tn.pfe.CnotConnectV1.services.interfaces.IPerformanceMetricsService;

@RestController
@RequestMapping("/api/performance-metrics")
@CrossOrigin(origins = "http://localhost:3000") 
public class PerformanceMetricsController {

    private final IPerformanceMetricsService performanceMetricsService;

    @Autowired
    public PerformanceMetricsController(IPerformanceMetricsService performanceMetricsService) {
        this.performanceMetricsService = performanceMetricsService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPerformanceMetrics(@RequestBody PerformanceMetrics performanceMetrics) {
        performanceMetricsService.createPerformanceMetrics(performanceMetrics);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updatePerformanceMetrics(@RequestBody PerformanceMetrics performanceMetrics) {
        performanceMetricsService.updatePerformanceMetrics(performanceMetrics);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<PerformanceMetrics> getPerformanceMetricsByAthlete(@PathVariable("athleteId") Long athleteId) {
        PerformanceMetrics performanceMetrics = performanceMetricsService.getPerformanceMetricsByAthlete(athleteId);
        if (performanceMetrics != null) {
            return ResponseEntity.ok().body(performanceMetrics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/generate/{athleteId}")
    public ResponseEntity<Void> generatePerformanceMetricsForAthlete(@PathVariable("athleteId") Long athleteId) {
        // Retrieve athlete from the database
        Athlete athlete = new Athlete(); 
        athlete.setAthleteId(athleteId); 

        performanceMetricsService.generatePerformanceMetricsForAthlete(athlete);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * Trigger metrics calculation for a specific game.
     * 
     * @param gameId ID of the game for which metrics should be calculated
     * @return ResponseEntity with the result of the operation
     */
    @PostMapping("/calculate/game/{gameId}")
    public ResponseEntity<String> calculateMetricsAfterGame(@PathVariable Long gameId) {
        try {
            performanceMetricsService.calculateAndSaveMetricsAfterGame(gameId);
            return ResponseEntity.ok("Performance metrics calculated successfully for game ID: " + gameId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<?> getMetricsByAthlete(@PathVariable Long athleteId) {
        try {
            
            List<PerformanceMetrics> metrics = performanceMetricsService.getMetricsByAthlete(athleteId);
            return ResponseEntity.ok(metrics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieve performance metrics for all athletes.
     * 
     * @return List of all performance metrics
     */
    @GetMapping("/all")
    public ResponseEntity<List<PerformanceMetrics>> getAllMetrics() {
        try {
            List<PerformanceMetrics> metrics = performanceMetricsService.getAllMetrics();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}