package tn.pfe.CnotConnectV1.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.Archive;
import tn.pfe.CnotConnectV1.entities.Event;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.Result;
import tn.pfe.CnotConnectV1.repository.ArchiveRepository;
import tn.pfe.CnotConnectV1.repository.EventRepository;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.repository.ResultRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IArchiveService;

@Service
public class ArchiveService implements IArchiveService {

    @Autowired
    private ArchiveRepository archiveRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ResultRepository resultRepository;
    
    
    @Override
    public Archive saveArchive(Archive archive) {
        return archiveRepository.save(archive);
    }
    @Override

    public List<Archive> getAllArchives() {
        return archiveRepository.findAll();
    }
    @Override

    public Archive getArchiveById(Long archiveId) {
        return archiveRepository.findById(archiveId).orElse(null);
    }
    @Override

    public void deleteArchiveById(Long archiveId) {
        archiveRepository.deleteById(archiveId);
    }
    @Override

    public Archive archiveEntities(Long eventId, Long gameId, Long resultId) {
        // Retrieve the entities to be archived
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        Result result = resultRepository.findById(resultId).orElseThrow(() -> new RuntimeException("Result not found"));

        // Create a new archive entity and set the archived entities
        Archive archive = new Archive();
       // archive.setEvent(event);
      //  archive.setGame(game);
      //  archive.setResult(result);
        archive.setDate(LocalDate.now()); 
        archive.setName(event.getName());
        // Save the archive entity
        archiveRepository.save(archive);

        // Set the archive attribute of the entities
        event.setArchive(archive);
        game.setArchive(archive);
        result.setArchive(archive);

        // Save the updated entities
        eventRepository.save(event);
        gameRepository.save(game);
        resultRepository.save(result);

        return archive;
    }
}
