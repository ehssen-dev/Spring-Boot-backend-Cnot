package tn.pfe.CnotConnectV1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.EventDTO;
import tn.pfe.CnotConnectV1.entities.Event;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.repository.EventRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IEventService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {
    
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event saveEvent(EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        return eventRepository.save(event);
    }

    private Event convertToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventId(eventDTO.getEventId());
        event.setName(eventDTO.getName());
        event.setStartEvent(eventDTO.getStartEvent());
        event.setEndEvent(eventDTO.getEndEvent());
        event.setDescription(eventDTO.getDescription());


        return event;
    }
    @Override
    public Event updateEvent(Long eventId, EventDTO eventDTO) {
       
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        existingEvent.setName(eventDTO.getName());
        existingEvent.setStartEvent(eventDTO.getStartEvent());
        existingEvent.setEndEvent(eventDTO.getEndEvent());
        existingEvent.setDescription(eventDTO.getDescription());

        return eventRepository.save(existingEvent);
    }

    

    @Override
    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public boolean eventHasEnded(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        return eventOptional.map(event -> LocalDate.now().isAfter(event.getEndEvent())).orElse(false);
    }
    @Override
    public String evaluateEventProgress() {
        List<Event> events = eventRepository.findAll(); 
        StringBuilder evaluationSummary = new StringBuilder(); 
        
        for (Event event : events) {
            int completedGames = 0;
            int totalGames = event.getGames().size(); 

            for (Game game : event.getGames()) {
                if (game.isReadyForResult()) {
                    completedGames++;
                }
            }

            if (completedGames == totalGames) {
                String message = "Event " + event.getName() + " is complete with all games finished.";
                System.out.println(message);
                evaluationSummary.append(message).append("\n");
            } else {
                String message = "Event " + event.getName() + " is ongoing. " + completedGames + "/" + totalGames + " games are completed.";
                System.out.println(message);
                evaluationSummary.append(message).append("\n");
            }
        }

        return evaluationSummary.toString();
    }

    public List<Game> getEventGames(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return event.getGames(); 
    }

    public int getTotalGames(Long eventId) {
        List<Game> games = getEventGames(eventId);
        return games.size();
    }
}
