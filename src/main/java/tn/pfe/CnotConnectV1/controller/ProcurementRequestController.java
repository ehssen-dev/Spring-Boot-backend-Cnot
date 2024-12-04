package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.enums.RequestStatus;
import tn.pfe.CnotConnectV1.services.interfaces.IProcurementRequestService;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/procurement-requests")
@CrossOrigin(origins = "http://localhost:3000") 
public class ProcurementRequestController {

    private final IProcurementRequestService procurementRequestService;

    @Autowired
    public ProcurementRequestController(IProcurementRequestService procurementRequestService) {
        this.procurementRequestService = procurementRequestService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProcurementRequestDTO> createProcurementRequest(
            @Valid @RequestBody ProcurementRequestDTO procurementRequestDTO) {
        try {
        	 System.out.println("Received DTO: " + procurementRequestDTO);
            ProcurementRequest createdRequest = procurementRequestService.createProcurementRequest(procurementRequestDTO);
            ProcurementRequestDTO responseDTO = procurementRequestService.mapToDTO(createdRequest); 
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProcurementRequestDTO>> getAllProcurementRequests() {
        List<ProcurementRequestDTO> procurementRequests = procurementRequestService.getAllProcurementRequests();
        return ResponseEntity.ok(procurementRequests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ProcurementRequest> getProcurementRequestById(@PathVariable Long requestId) {
        ProcurementRequest procurementRequest = procurementRequestService.findProcurementRequestById(requestId);
        return ResponseEntity.ok(procurementRequest);
    }


    @PutMapping("/{requestId}")
    public ResponseEntity<ProcurementRequest> updateProcurementRequest(
            @PathVariable("requestId") Long requestId,
            @RequestBody ProcurementRequestDTO updatedRequest) {
        ProcurementRequest updated = procurementRequestService.updateProcurementRequest(requestId, updatedRequest);
        return ResponseEntity.ok().body(updated);
    }


    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteProcurementRequest(@PathVariable("requestId") Long requestId) {
        procurementRequestService.deleteProcurementRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ProcurementRequest>> getPendingRequests() {
        List<ProcurementRequest> pendingRequests = procurementRequestService.getPendingRequests();
        return ResponseEntity.ok().body(pendingRequests);
    }

    @PutMapping("/{requestId}/update-status")
    public ResponseEntity<ProcurementRequest> updateRequestStatus(
            @PathVariable("requestId") Long requestId,
            @RequestParam("status") RequestStatus status) {
        ProcurementRequest updatedRequest = procurementRequestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok().body(updatedRequest);
    }
    
    /////////////////////
    
    @PostMapping("/{requestId}/process")
    public ResponseEntity<String> processRequest(
            @PathVariable Long requestId,
            @RequestBody Map<String, Object> requestBody) {

        boolean approved = Boolean.parseBoolean(requestBody.get("approved").toString());
        String comments = requestBody.get("comments").toString();

        try {
            procurementRequestService.processRequest(requestId, approved, comments);
            return ResponseEntity.ok("Request processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process request: " + e.getMessage());
        }
    }
    
    @PostMapping("/{requestId}/submit")
    public ResponseEntity<String> submitRequestForApproval(@PathVariable Long requestId) {
        try {
            ProcurementRequest request = procurementRequestService.findProcurementRequestById(requestId);
            procurementRequestService.submitRequestForApproval(request);
            return ResponseEntity.ok("Request submitted for approval.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Approve or reject a request
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId ,@RequestBody String comments ) {
        try {
            ProcurementRequest request = procurementRequestService.findProcurementRequestById(requestId);
        	procurementRequestService.approveStage(request, comments);
            return ResponseEntity.ok("Request rejected successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reset the status of a request
    @PostMapping("/{requestId}/reset")
    public ResponseEntity<String> resetRequest(@PathVariable Long requestId) {
        try {
        	procurementRequestService.resetRequest(requestId);
            return ResponseEntity.ok("Request status reset to PENDING.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/{requestId}/rejectR")
    public ResponseEntity<String> rejectRequest(@PathVariable Long requestId, 
                                                @RequestBody String comments) {
        try {
            ProcurementRequest request = procurementRequestService.findProcurementRequestById(requestId);
            procurementRequestService.rejectRequest(request, comments);
            return ResponseEntity.ok("Request rejected successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to approve a procurement request
    @PostMapping("/{requestId}/approveR")
    public ResponseEntity<String> approveStage(@PathVariable Long requestId, 
                                               @RequestBody String comments) {
        try {
            ProcurementRequest request = procurementRequestService.findProcurementRequestById(requestId);
            procurementRequestService.approveStage(request, comments);
            return ResponseEntity.ok("Request approved successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    
}