package tn.pfe.CnotConnectV1.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.GameDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Event;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.EventRepository;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IGameService;

@Service
public class GameService implements IGameService {
    
    private final GameRepository gameRepository;
    @Autowired
    private  EventRepository eventRepository;
    @Autowired
    private AthleteRepository athleteRepository;
    
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game saveGame(GameDTO gameDTO) {
        Game game = new Game();
        game.setGameId(gameDTO.getGameId());
        game.setName(gameDTO.getName());
        game.setDate(gameDTO.getDate());
        game.setLocation(gameDTO.getLocation());
        game.setDescription(gameDTO.getDescription());
        game.setStartGame(gameDTO.getStartGame());
        game.setEndGame(gameDTO.getEndGame());

        if (gameDTO.getEventId() != null) {
            Event event = eventRepository.findById(gameDTO.getEventId()).orElse(null);
            game.setEvent(event);
        }
        return gameRepository.save(game);
    }
    
    @Override
    public Game updateGame(Long gameId, GameDTO gameDTO) {
       
        Game existingGame = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game not found with id " + gameId));
       
        existingGame.setName(gameDTO.getName());
        existingGame.setDate(gameDTO.getDate());
        existingGame.setLocation(gameDTO.getLocation());
        existingGame.setDescription(gameDTO.getDescription());
        existingGame.setStartGame(gameDTO.getStartGame());
        existingGame.setEndGame(gameDTO.getEndGame());

        if (gameDTO.getEventId() != null) {
            Event event = eventRepository.findById(gameDTO.getEventId()).orElse(null);
            existingGame.setEvent(event);
        }

        return gameRepository.save(existingGame);
    }

    
    @Override
    public Optional<Game> getGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public void deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    
    @Override
    public boolean gameHasEnded(Long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        return gameOptional.map(Game::getEndGame)
                           .map(endGame -> LocalDate.now().isAfter(endGame))
                           .orElse(false);
    }
    
    @Override
    public void addAthleteToGame(Long athleteId, Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));
        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new IllegalArgumentException("Athlete not found with ID: " + athleteId));
        
        athlete.participateInGame(game);  
        athleteRepository.save(athlete);  
        gameRepository.save(game);        
    }

    @Override
    public void removeAthleteFromGame(Long athleteId, Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));
        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new IllegalArgumentException("Athlete not found with ID: " + athleteId));
        
        athlete.leaveGame();  
        athleteRepository.save(athlete); 
        gameRepository.save(game);        
    }
    
    
}