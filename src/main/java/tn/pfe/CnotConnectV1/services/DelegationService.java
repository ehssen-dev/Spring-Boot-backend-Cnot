package tn.pfe.CnotConnectV1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.DelegationDTO;
import tn.pfe.CnotConnectV1.entities.Delegation;
import tn.pfe.CnotConnectV1.repository.DelegationRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IDelegationService;

@Service
public class DelegationService implements IDelegationService {
    @Autowired
    DelegationRepository delegationRepository;
    
    
    public List<String> getAllEmails() {
        return delegationRepository.findAll().stream()
                .map(Delegation::getEmail)
                .collect(Collectors.toList());
    }
    @Override
    public List<Delegation> retrieveAllDelegations() {
        return delegationRepository.findAll();
    }
    @Override
    public Delegation getDelegationById(Long delegationId) {
        return delegationRepository.findById(delegationId)
                .orElseThrow(() -> new EntityNotFoundException("Delegation with ID " + delegationId + " not found"));
    }

    @Override
    public DelegationDTO createDelegation(DelegationDTO delegationDTO) {
        Delegation delegation = new Delegation();
        delegation.setDelegationName(delegationDTO.getDelegationName());
        delegation.setCountry(delegationDTO.getCountry());
        delegation.setEmail(delegationDTO.getEmail());
        // Add other properties if necessary

        Delegation createdDelegation = delegationRepository.save(delegation);
        return convertToDTO(createdDelegation);
    }

    @Override
    public DelegationDTO updateDelegation(Long delegationId, DelegationDTO delegationDTO) {
        Delegation existingDelegation = getDelegationById(delegationId);
        
        existingDelegation.setDelegationName(delegationDTO.getDelegationName());
        existingDelegation.setCountry(delegationDTO.getCountry());
        existingDelegation.setEmail(delegationDTO.getEmail());
        // Update other properties if necessary

        Delegation updatedDelegation = delegationRepository.save(existingDelegation);
        return convertToDTO(updatedDelegation);
    }

    private DelegationDTO convertToDTO(Delegation delegation) {
        DelegationDTO dto = new DelegationDTO();
        dto.setDelegationId(delegation.getDelegationId());
        dto.setDelegationName(delegation.getDelegationName());
        dto.setCountry(delegation.getCountry());
        dto.setEmail(delegation.getEmail());
        // Set other properties if necessary
        return dto;
    }

    private Delegation convertToEntity(DelegationDTO dto) {
        Delegation delegation = new Delegation();
        delegation.setDelegationId(dto.getDelegationId());
        delegation.setDelegationName(dto.getDelegationName());
        delegation.setCountry(dto.getCountry());
        delegation.setEmail(dto.getEmail());
        // Set other properties if necessary
        return delegation;
    }

    @Override
    public void deleteDelegation(Long delegationId) {
        Delegation delegation = getDelegationById(delegationId);
        delegationRepository.delete(delegation);
    }
}