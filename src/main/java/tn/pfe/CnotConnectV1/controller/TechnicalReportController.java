package tn.pfe.CnotConnectV1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.TechnicalReportDTO;
import tn.pfe.CnotConnectV1.entities.TechnicalReport;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.services.interfaces.ITechnicalReportService;

@RestController
@RequestMapping("/api/technical-reports")
@CrossOrigin(origins = "http://localhost:3000") 
public class TechnicalReportController {
    
    @Autowired
    private ITechnicalReportService technicalReportService;

    @GetMapping("/all")
    public List<TechnicalReport> getAllTechnicalReports() {
        return technicalReportService.findAll();
    }

    @GetMapping("/{tReportId}")
    public ResponseEntity<TechnicalReport> getTechnicalReportById(@PathVariable Long tReportId) {
        Optional<TechnicalReport> technicalReport = technicalReportService.findById(tReportId);
        return technicalReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

   
    
    @PostMapping("/{projectId}")
    public ResponseEntity<?> createTechnicalReport(
            @PathVariable Long projectId,
            @RequestBody TechnicalReportDTO reportDTO) {
        try {
            TechnicalReport createdReport = technicalReportService.createTechnicalReport(projectId, reportDTO);
            return ResponseEntity.ok(createdReport);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    
    @PutMapping("/{tReportId}")
    public ResponseEntity<?> updateTechnicalReport(
            @PathVariable Long tReportId,
            @RequestBody TechnicalReportDTO reportUpdateDTO) {
        try {
            // Call the service method to update the report
            TechnicalReportDTO updatedReportDTO = technicalReportService.updateTechnicalReport(tReportId, reportUpdateDTO);
            return ResponseEntity.ok(updatedReportDTO);
        } catch (ResourceNotFoundException e) {
            // Return a 404 status if the report is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Return a 400 status for bad request errors
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Return a 500 status for internal server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTechnicalReportByProjectId(@PathVariable Long projectId) {
        try {
            TechnicalReport report = technicalReportService.getTechnicalReportByProjectId(projectId);
            return ResponseEntity.ok(report);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @DeleteMapping("/{tReportId}")
    public ResponseEntity<Void> deleteTechnicalReport(@PathVariable Long tReportId) {
        technicalReportService.deleteById(tReportId);
        return ResponseEntity.noContent().build();
    }
}