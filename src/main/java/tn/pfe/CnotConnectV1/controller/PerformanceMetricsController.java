package tn.pfe.CnotConnectV1.controller;

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
        Athlete athlete = new Athlete(); // Replace with your method to fetch athlete by ID
        athlete.setAthleteId(athleteId); // Set athlete ID

        // Generate performance metrics for the athlete
        performanceMetricsService.generatePerformanceMetricsForAthlete(athlete);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}