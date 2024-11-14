package tn.pfe.CnotConnectV1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.entities.Archive;
import tn.pfe.CnotConnectV1.services.interfaces.IArchiveService;

@RestController
@RequestMapping("/api/archives")
@CrossOrigin(origins = "http://localhost:3000") 
public class ArchiveController {

    @Autowired
    private IArchiveService archiveService;

    @PostMapping
    public ResponseEntity<Archive> saveArchive(@RequestBody Archive archive) {
        Archive savedArchive = archiveService.saveArchive(archive);
        return ResponseEntity.ok(savedArchive);
    }

    @GetMapping
    public ResponseEntity<List<Archive>> getAllArchives() {
        List<Archive> archives = archiveService.getAllArchives();
        return ResponseEntity.ok(archives);
    }

    @GetMapping("/{archiveId}")
    public ResponseEntity<Archive> getArchiveById(@PathVariable Long archiveId) {
        Archive archive = archiveService.getArchiveById(archiveId);
        if (archive != null) {
            return ResponseEntity.ok(archive);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{archiveId}")
    public ResponseEntity<Void> deleteArchiveById(@PathVariable Long archiveId) {
        archiveService.deleteArchiveById(archiveId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/archiveEntities")
    public ResponseEntity<Archive> archiveEntities(
            @RequestParam Long eventId,
            @RequestParam Long gameId,
            @RequestParam Long resultId) {
        try {
            Archive archive = archiveService.archiveEntities(eventId, gameId, resultId);
            return ResponseEntity.ok(archive);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}