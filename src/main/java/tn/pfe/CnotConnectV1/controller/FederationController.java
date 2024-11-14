package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.FederationDTO;
import tn.pfe.CnotConnectV1.entities.Federation;
import tn.pfe.CnotConnectV1.services.interfaces.IFederationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/federations")
public class FederationController {

    @Autowired
    private IFederationService federationService;

  
    @GetMapping("/all")
    public ResponseEntity<List<Federation>> getAllFederations() {
        List<Federation> federations = federationService.getAllFederations();
        return new ResponseEntity<>(federations, HttpStatus.OK);
    }

    @GetMapping("/{federationId}")
    public ResponseEntity<Federation> getFederationById(@PathVariable Long federationId) {
        Optional<Federation> federation = federationService.getFederationById(federationId);
        return federation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/add")
    public ResponseEntity<FederationDTO> createOrUpdateFederation(@RequestBody FederationDTO federationDTO) {
        FederationDTO savedFederationDTO = federationService.saveFederation(federationDTO);
        return new ResponseEntity<>(savedFederationDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{federationId}")
    public ResponseEntity<Void> deleteFederation(@PathVariable Long federationId) {
        federationService.deleteFederation(federationId);
        return ResponseEntity.noContent().build();
    }
}

