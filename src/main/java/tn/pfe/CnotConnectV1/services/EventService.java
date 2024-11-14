package tn.pfe.CnotConnectV1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.EventDTO;
import tn.pfe.CnotConnectV1.entities.Event;
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
        // Fetch the existing event by ID
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        // Update the event's fields with new values from eventDTO
        existingEvent.setName(eventDTO.getName());
        existingEvent.setStartEvent(eventDTO.getStartEvent());
        existingEvent.setEndEvent(eventDTO.getEndEvent());
        existingEvent.setDescription(eventDTO.getDescription());

        // Save and return the updated event
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
}
