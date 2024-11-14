package tn.pfe.CnotConnectV1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.EventDTO;
import tn.pfe.CnotConnectV1.entities.Event;
import tn.pfe.CnotConnectV1.services.EventService;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000") 
public class EventController {
    
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/add")
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event savedEvent = eventService.saveEvent(eventDTO);
        EventDTO responseDTO = convertToDTO(savedEvent);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO eventDTO) {
        Event updatedEvent = eventService.updateEvent(eventId, eventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setEventId(event.getEventId());
        dto.setName(event.getName());
        dto.setStartEvent(event.getStartEvent());
        dto.setEndEvent(event.getEndEvent());
        dto.setDescription(event.getDescription());
 

        return dto;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        return eventOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hasEnded/{eventId}")
    public ResponseEntity<Boolean> eventHasEnded(@PathVariable Long eventId) {
        boolean hasEnded = eventService.eventHasEnded(eventId);
        return ResponseEntity.ok(hasEnded);
    }
}