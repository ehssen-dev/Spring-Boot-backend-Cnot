package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.GameDTO;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.services.GameService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:3000") 
public class GameController {
    
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/add")
    public Game createGame(@RequestBody GameDTO gameDTO) {
        return gameService.saveGame(gameDTO);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<Game> updateGame(@PathVariable Long gameId, @RequestBody GameDTO gameDTO) {
        Game updatedGame = gameService.updateGame(gameId, gameDTO);
        return ResponseEntity.ok(updatedGame);
    }
    
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        Optional<Game> gameOptional = gameService.getGameById(gameId);
        return gameOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{gameId}/hasEnded")
    public ResponseEntity<Boolean> gameHasEnded(@PathVariable Long gameId) {
        boolean hasEnded = gameService.gameHasEnded(gameId);
        return ResponseEntity.ok(hasEnded);
    }
    
    @PostMapping("/{gameId}/athletes/{athleteId}")
    public ResponseEntity<String> addAthleteToGame(@PathVariable Long gameId, @PathVariable Long athleteId) {
        try {
            gameService.addAthleteToGame(athleteId, gameId);
            return ResponseEntity.ok("Athlete added to game successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{gameId}/athletes/{athleteId}")
    public ResponseEntity<String> removeAthleteFromGame(@PathVariable Long gameId, @PathVariable Long athleteId) {
        try {
            gameService.removeAthleteFromGame(athleteId, gameId);
            return ResponseEntity.ok("Athlete removed from game successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}