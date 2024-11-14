package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.GameDTO;
import tn.pfe.CnotConnectV1.entities.Game;

public interface IGameService {
	
	  //Game saveGame(Game game);
	    
	    Optional<Game> getGameById(Long gameId);
	    
	    List<Game> getAllGames();
	    
	    void deleteGame(Long gameId);
	    
	    boolean gameHasEnded(Long gameId);

		Game saveGame(GameDTO gameDTO);

		void removeAthleteFromGame(Long athleteId, Long gameId);

		void addAthleteToGame(Long athleteId, Long gameId);

		Game updateGame(Long gameId, GameDTO gameDTO);

}
