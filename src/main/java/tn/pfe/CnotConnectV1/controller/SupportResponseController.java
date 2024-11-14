package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.SupportResponseDTO;
import tn.pfe.CnotConnectV1.entities.SupportResponse;
import tn.pfe.CnotConnectV1.services.interfaces.ISupportResponseService;

import java.util.List;

@RestController
@RequestMapping("/api/support-responses")
@CrossOrigin(origins = "http://localhost:3000") 
public class SupportResponseController {

    private final ISupportResponseService supportResponseService;

    @Autowired
    public SupportResponseController(ISupportResponseService supportResponseService) {
        this.supportResponseService = supportResponseService;
    }

   
    @PostMapping("/add")
    public ResponseEntity<SupportResponseDTO> createSupportResponse(@RequestBody SupportResponseDTO supportResponseDTO) {
        SupportResponseDTO createdResponse = supportResponseService.createSupportResponse(supportResponseDTO);
        return new ResponseEntity<>(createdResponse, HttpStatus.CREATED);
    }

    @GetMapping("/by-request/{supportRequestId}")
    public ResponseEntity<List<SupportResponse>> getSupportResponsesByRequest(@PathVariable Long supportRequestId) {
        try {
            List<SupportResponse> responses = supportResponseService.getSupportResponsesByRequest(supportRequestId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{supportResponseId}")
    public ResponseEntity<SupportResponse> getSupportResponseById(@PathVariable Long supportResponseId) {
        try {
            SupportResponse response = supportResponseService.getSupportResponseById(supportResponseId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*@GetMapping("/all")
    public ResponseEntity<List<SupportResponse>> getAllSupportRequests() {
        List<SupportResponse> supportRequests = supportResponseService.getAllSupportResponses();
        return ResponseEntity.ok(supportRequests);
    }*/

    @DeleteMapping("/delete/{supportResponseId}")
    public ResponseEntity<Void> deleteSupportResponse(@PathVariable Long supportResponseId) {
        try {
            supportResponseService.deleteSupportResponse(supportResponseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<SupportResponseDTO>> getAllSupportResponses() {
        List<SupportResponseDTO> responseDTOs = supportResponseService.getAllSupportResponsesDTO();
        return ResponseEntity.ok(responseDTOs);
    }
    
    @PutMapping("/update")
    public ResponseEntity<SupportResponse> updateSupportResponse(@RequestBody SupportResponse response) {
        try {
            SupportResponse updatedResponse = supportResponseService.updateSupportResponse(response);
            return ResponseEntity.ok(updatedResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/add/{supportRequestId}")
    public ResponseEntity<SupportResponse> addSupportResponse(@PathVariable Long supportRequestId,
                                                              @RequestBody SupportResponse response) {
        try {
            SupportResponse addedResponse = supportResponseService.addSupportResponse(supportRequestId, response);
            return new ResponseEntity<>(addedResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}