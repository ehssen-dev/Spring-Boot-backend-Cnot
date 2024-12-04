package tn.pfe.CnotConnectV1.Scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.Event;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.Result;
import tn.pfe.CnotConnectV1.repository.EventRepository;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.repository.ResultRepository;

@Service
public class MonitoringService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void evaluateGamesAndEvents() {
        StringBuilder status = new StringBuilder();

        try {
            // Evaluate all games
            List<Game> games = gameRepository.findAll();
            for (Game game : games) {
                try {
                    game.evaluateGameProgress();
                    status.append("Game with ID: ").append(game.getGameId()).append(" evaluated successfully.\n");
                } catch (Exception e) {
                    status.append("Error evaluating Game with ID: ").append(game.getGameId()).append(": ").append(e.getMessage()).append("\n");
                }
            }

            // Evaluate all results
            List<Result> results = resultRepository.findAll();
            for (Result result : results) {
                try {
                    result.evaluateResult();
                    status.append("Result with ID: ").append(result.getResultId()).append(" evaluated successfully.\n");
                } catch (Exception e) {
                    status.append("Error evaluating Result with ID: ").append(result.getResultId()).append(": ").append(e.getMessage()).append("\n");
                }
            }

            // Evaluate all events
            List<Event> events = eventRepository.findAll();
            for (Event event : events) {
                try {
                    event.evaluateEventProgress();
                    status.append("Event with ID: ").append(event.getEventId()).append(" evaluated successfully.\n");
                } catch (Exception e) {
                    status.append("Error evaluating Event with ID: ").append(event.getEventId()).append(": ").append(e.getMessage()).append("\n");
                }
            }
        } catch (Exception e) {
            status.append("General error during evaluation: ").append(e.getMessage());
        }

        System.out.println(status.toString()); 
    }

}

