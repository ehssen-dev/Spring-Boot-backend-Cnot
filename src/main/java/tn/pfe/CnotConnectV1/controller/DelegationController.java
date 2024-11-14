package tn.pfe.CnotConnectV1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.DelegationDTO;
import tn.pfe.CnotConnectV1.entities.Delegation;
import tn.pfe.CnotConnectV1.services.interfaces.IDelegationService;

@RestController
@RequestMapping("/api/delegations")
@CrossOrigin(origins = "http://localhost:3000") 
public class DelegationController {

    @Autowired
    private IDelegationService delegationService;

    @GetMapping("/all")
    public List<Delegation> getAllDelegations() {
        return delegationService.retrieveAllDelegations();
    }

    @GetMapping("/{delegationId}")
    public ResponseEntity<Delegation> getDelegationById(@PathVariable Long delegationId) {
        Delegation delegation = delegationService.getDelegationById(delegationId);
        return ResponseEntity.ok(delegation);
    }

    @PostMapping("/add")
    public ResponseEntity<DelegationDTO> createDelegation(@RequestBody DelegationDTO delegationDTO) {
        DelegationDTO createdDelegation = delegationService.createDelegation(delegationDTO);
        return ResponseEntity.ok(createdDelegation);
    }

    @PutMapping("/{delegationId}")
    public ResponseEntity<DelegationDTO> updateDelegation(@PathVariable Long delegationId, @RequestBody DelegationDTO delegationDTO) {
        DelegationDTO updatedDelegation = delegationService.updateDelegation(delegationId, delegationDTO);
        return ResponseEntity.ok(updatedDelegation);
    }

    @DeleteMapping("/{delegationId}")
    public ResponseEntity<Void> deleteDelegation(@PathVariable Long delegationId) {
        delegationService.deleteDelegation(delegationId);
        return ResponseEntity.noContent().build();
    }
}