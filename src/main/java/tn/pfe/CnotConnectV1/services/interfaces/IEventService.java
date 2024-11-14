package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.EventDTO;
import tn.pfe.CnotConnectV1.entities.Event;

public interface IEventService {
//Event saveEvent(Event event);
    
    Optional<Event> getEventById(Long eventId);
    
    List<Event> getAllEvents();
    
    void deleteEvent(Long eventId);
    
    boolean eventHasEnded(Long eventId);

	Event saveEvent(EventDTO eventDTO);

	Event updateEvent(Long eventId, EventDTO eventDTO);

}
