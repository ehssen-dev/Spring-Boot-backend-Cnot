package tn.pfe.CnotConnectV1.controller;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import tn.pfe.CnotConnectV1.dto.SupportRequestDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Federation;
import tn.pfe.CnotConnectV1.entities.SupportRequest;
import tn.pfe.CnotConnectV1.services.interfaces.ISupportRequestService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/support-requests")
@CrossOrigin(origins = "http://localhost:3000") 
public class SupportRequestController {

    private final ISupportRequestService supportRequestService;
    private static final Logger log = LoggerFactory.getLogger(SupportRequestController.class);

    @Autowired
    public SupportRequestController(ISupportRequestService supportRequestService) {
        this.supportRequestService = supportRequestService;
    }

    @PostMapping(value = "/from-athlete/{athleteId}", consumes = "application/json")
    public ResponseEntity<SupportRequest> createSupportRequestFromAthlete(
            @PathVariable Long athleteId,
            @RequestBody SupportRequestDTO supportRequestDTO) {

        // Call the service to create the support request
        SupportRequest supportRequest = supportRequestService.createSupportRequestFromAthlete(supportRequestDTO, athleteId);

        // Return the created support request as a response
        return new ResponseEntity<>(supportRequest, HttpStatus.CREATED);
    }

    /**
     * Creates a support request from a federation.
     *
     * @param federationId      the ID of the federation
     * @param supportRequestDTO the DTO containing support request details
     * @return the created support request
     */
    @PostMapping("/from-federation/{federationId}")
    public ResponseEntity<SupportRequest> createSupportRequestFromFederation(
            @PathVariable Long federationId,
            @RequestBody SupportRequestDTO supportRequestDTO) {
        
        SupportRequest supportRequest = supportRequestService.createSupportRequestFromFederation(supportRequestDTO, federationId);
        return new ResponseEntity<>(supportRequest, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<SupportRequest> updateSupportRequest(@RequestBody SupportRequest request) {
        try {
            SupportRequest updatedRequest = supportRequestService.updateSupportRequest(request);
            return ResponseEntity.ok(updatedRequest);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{supportRequestId}")
    public ResponseEntity<SupportRequest> getSupportRequestById(@PathVariable Long supportRequestId) {
        SupportRequest request = supportRequestService.getSupportRequestById(supportRequestId);
        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
  /*  @GetMapping
    public ResponseEntity<List<SupportRequest>> getAllSupportRequests() {
        try {
            List<SupportRequest> supportRequests = supportRequestService.getAllSupportRequests();
            return ResponseEntity.ok(supportRequests);
        } catch (Exception e) {
            // Log the exception if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    @GetMapping
    public ResponseEntity<List<SupportRequest>> getAllSupportRequests(@RequestParam Optional<String> status) {
        List<SupportRequest> supportRequests;
        if (status.isPresent()) {
            supportRequests = supportRequestService.getAllSupportRequests(status);
        } else {
            supportRequests = supportRequestService.getAllSupportRequests();
        }
        return ResponseEntity.ok(supportRequests);
    }
    @GetMapping("/all")
    public ResponseEntity<List<SupportRequest>> getAllSupportRequests() {
        List<SupportRequest> supportRequests = supportRequestService.getAllSupportRequests();
        return ResponseEntity.ok(supportRequests);
    }
    @GetMapping("/alls")
    public ResponseEntity<List<SupportRequestDTO>> getAllSupportRequestsDTO() {
        List<SupportRequestDTO> supportRequests = supportRequestService.getAllSupportRequestsDTO();
        return new ResponseEntity<>(supportRequests, HttpStatus.OK);
    }
    @GetMapping("/by-athlete/{athleteId}")
    public ResponseEntity<List<SupportRequest>> getSupportRequestsByAthlete(@PathVariable Long athleteId) {
        List<SupportRequest> supportRequests = supportRequestService.getSupportRequestsByAthlete(athleteId);
        return ResponseEntity.ok(supportRequests);
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<Void> deleteSupportRequest(@PathVariable Long requestId) {
        try {
            supportRequestService.deleteSupportRequest(requestId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{supportRequestId}/change-status")
    public ResponseEntity<Void> changeRequestStatus(@PathVariable Long supportRequestId,
                                                    @RequestParam String newStatus) {
        try {
            supportRequestService.changeRequestStatus(supportRequestId, newStatus);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}