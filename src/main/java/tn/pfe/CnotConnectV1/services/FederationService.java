package tn.pfe.CnotConnectV1.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.FederationDTO;
import tn.pfe.CnotConnectV1.entities.Federation;
import tn.pfe.CnotConnectV1.repository.FederationRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IFederationService;

@Service
public class FederationService implements IFederationService{

    @Autowired
    private FederationRepository federationRepository;

    @Override
    public List<Federation> getAllFederations() {
        return federationRepository.findAll();
    }

    @Override
    public Optional<Federation> getFederationById(Long federationId) {
        return federationRepository.findById(federationId);
    }
    @Override
    public void deleteFederation(Long federationId) {
        federationRepository.deleteById(federationId);
    }
    @Override
    public FederationDTO saveFederation(FederationDTO federationDTO) {
        Federation federation = convertToEntity(federationDTO);
        String generatedFederationNumber = generateFederationNumber(); // Generate federation number
        federation.setFederationNumber(generatedFederationNumber);
        Federation savedFederation = federationRepository.save(federation);
        return convertToDTO(savedFederation);
    }

    private static final String ALPHANUMERIC_CHARACTERS = "0123456789";
    private static final int LENGTH = 4;

    private String generateFederationNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH + 2); 
        sb.append('F');
        sb.append('-');

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }

        return sb.toString();
    }


    private FederationDTO convertToDTO(Federation federation) {
        return new FederationDTO(
            federation.getFederationId(),
            federation.getFederationNumber(),
            federation.getName(),
            federation.getDescription(),
            federation.getEmail()
        );
    }

    private Federation convertToEntity(FederationDTO federationDTO) {
        Federation federation = new Federation();
        federation.setFederationId(federationDTO.getFederationId());
        federation.setFederationNumber(federationDTO.getFederationNumber());
        federation.setName(federationDTO.getName());
        federation.setDescription(federationDTO.getDescription());
        federation.setEmail(federationDTO.getEmail());
        return federation;
    }
}