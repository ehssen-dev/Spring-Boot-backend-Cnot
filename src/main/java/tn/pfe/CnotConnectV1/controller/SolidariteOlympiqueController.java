package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.services.interfaces.ISolidariteOlympiqueService;

import java.util.List;

@RestController
@RequestMapping("/api/solidarite-olympique")
@CrossOrigin(origins = "http://localhost:3000") 
public class SolidariteOlympiqueController {

    private final ISolidariteOlympiqueService solidariteOlympiqueService;

    @Autowired
    public SolidariteOlympiqueController(ISolidariteOlympiqueService solidariteOlympiqueService) {
        this.solidariteOlympiqueService = solidariteOlympiqueService;
    }

    @PostMapping("/create")
    public ResponseEntity<SolidarityOlympic> createSolidariteOlympique(@RequestBody SolidarityOlympic program) {
        SolidarityOlympic createdProgram = solidariteOlympiqueService.createSolidariteOlympique(program);
        return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
    }

    @GetMapping("/{solidarityOlympicId}")
    public ResponseEntity<SolidarityOlympic> getSolidariteOlympiqueProgram(@PathVariable Long solidarityOlympicId) {
        SolidarityOlympic program = solidariteOlympiqueService.getSolidariteOlympiqueProgram(solidarityOlympicId);
        return ResponseEntity.ok(program);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SolidarityOlympic>> getAllSolidariteOlympiquePrograms() {
        List<SolidarityOlympic> programs = solidariteOlympiqueService.getAllSolidariteOlympiquePrograms();
        return ResponseEntity.ok(programs);
    }

    @PostMapping("/{solidarityOlympicId}/add-project")
    public ResponseEntity<SolidarityOlympic> addProjectToSolidariteOlympique(@PathVariable Long solidarityOlympicId,
                                                                             @RequestBody Project project) {
        SolidarityOlympic updatedProgram = solidariteOlympiqueService.addProjectToSolidarityOlympic(solidarityOlympicId, project);
        return ResponseEntity.ok(updatedProgram);
    }

    @PostMapping("/{solidarityOlympicId}/add-project-by-id")
    public ResponseEntity<Void> addProjectToSolidariteOlympiqueById(@PathVariable Long solidarityOlympicId,
                                                                    @RequestParam Long projectId) {
        solidariteOlympiqueService.addProjectToSolidariteOlympiqueProgram(solidarityOlympicId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{solidarityOlympicId}/remove-project-by-id")
    public ResponseEntity<Void> removeProjectFromSolidariteOlympiqueById(@PathVariable Long solidarityOlympicId,
                                                                         @RequestParam Long projectId) {
        solidariteOlympiqueService.removeProjectFromSolidariteOlympiqueProgram(solidarityOlympicId, projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{solidarityOlympicId}/projects")
    public ResponseEntity<List<Project>> getProjectsBySolidariteOlympiqueProgram(@PathVariable Long solidarityOlympicId) {
        List<Project> projects = solidariteOlympiqueService.getProjectsBySolidariteOlympiqueProgram(solidarityOlympicId);
        return ResponseEntity.ok(projects);
    }
}