package tn.pfe.CnotConnectV1.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.AthleteDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;
import tn.pfe.CnotConnectV1.services.interfaces.IAthleteService;

@RestController
@RequestMapping("/api/athletes")
@CrossOrigin(origins = "http://localhost:3000") 
public class AthleteController {

    @Autowired
    private IAthleteService athleteService;

    @GetMapping("/all")
    public ResponseEntity<List<AthleteDTO>> getAllAthletes() {
        List<AthleteDTO> athletes = athleteService.getAllAthletes();
        return ResponseEntity.ok(athletes);
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<Athlete> getAthleteById(@PathVariable Long athleteId) {
        if (athleteId == null || athleteId <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Athlete athlete = athleteService.getAthleteById(athleteId);
            return ResponseEntity.ok(athlete);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{athleteId}/associate/{federationId}")
    public ResponseEntity<String> associateAthleteWithFederation(
            @PathVariable Long athleteId,
            @PathVariable Long federationId) {
        try {
            athleteService.associateAthleteWithFederation(athleteId, federationId);
            return ResponseEntity.ok("Athlete associated with Federation successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/add")
    public ResponseEntity<AthleteDTO> addAthlete(@Valid @RequestBody AthleteDTO athleteDTO) {
        AthleteDTO savedAthlete = athleteService.addAthlete(athleteDTO);
        return ResponseEntity.ok(savedAthlete);
    }

    @PutMapping("/{athleteId}")
    public ResponseEntity<AthleteDTO> updateAthlete(
            @PathVariable("athleteId") Long athleteId,
            @Valid @RequestBody AthleteDTO athleteDTO) {

        AthleteDTO updatedAthlete = athleteService.updateAthlete(athleteId, athleteDTO);
        return ResponseEntity.ok(updatedAthlete);
    }

    @DeleteMapping("/{athleteId}")
    public ResponseEntity<Void> deleteAthlete(@PathVariable Long athleteId) {
        athleteService.deleteAthlete(athleteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{athleteId}/performanceMetrics")
    public ResponseEntity<PerformanceMetrics> createPerformanceMetrics(@PathVariable Long athleteId, @RequestBody PerformanceMetrics metrics) {
        try {
            PerformanceMetrics createdMetrics = athleteService.createPerformanceMetrics(athleteId, metrics);
            return ResponseEntity.ok(createdMetrics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{athleteId}/game")
    public ResponseEntity<Game> getAthleteGame(@PathVariable Long athleteId) {
        Game game = athleteService.getGameByAthleteId(athleteId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(game);
    }
    
    @GetMapping("/email")
    public ResponseEntity<Athlete> findByEmail(@RequestParam String email) {
        Optional<Athlete> athlete = athleteService.findByEmail(email);
        return athlete.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
}