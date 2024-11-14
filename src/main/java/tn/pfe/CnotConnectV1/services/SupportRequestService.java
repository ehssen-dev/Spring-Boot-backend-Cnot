package tn.pfe.CnotConnectV1.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.SupportRequestDTO;
import tn.pfe.CnotConnectV1.dto.SupportResponseDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Federation;
import tn.pfe.CnotConnectV1.entities.SupportRequest;
import tn.pfe.CnotConnectV1.entities.SupportResponse;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.FederationRepository;
import tn.pfe.CnotConnectV1.repository.SupportRequestRepository;
import tn.pfe.CnotConnectV1.services.interfaces.ISupportRequestService;

@Service
public class SupportRequestService  implements ISupportRequestService{

  @Autowired
  private SupportRequestRepository supportRequestRepository;

  @Autowired
  private AthleteRepository athleteRepository;
  @Autowired
  private FederationRepository federationRepository;
  
  @Override
  public SupportRequest createSupportRequestFromAthlete(SupportRequestDTO supportRequestDTO, Long athleteId) {
      // Step 1: Find the Athlete entity
      Athlete athlete = athleteRepository.findById(athleteId)
              .orElseThrow(() -> new RuntimeException("Athlete not found"));

    
      // Step 3: Create and populate the SupportRequest entity
      SupportRequest supportRequest = new SupportRequest();
      supportRequest.setSubject(supportRequestDTO.getSubject());
      supportRequest.setMontant(supportRequestDTO.getMontant());
      supportRequest.setJustification(supportRequestDTO.getJustification());
      supportRequest.setDescription(supportRequestDTO.getDescription());
      supportRequest.setPriority(supportRequestDTO.getPriority());
      supportRequest.setStatus("Pending Review");  // Default status when created

      // No attachments now, so just skip this part

      // Set the creation and update dates
      LocalDate now = LocalDate.now();
      supportRequest.setCreatedDate(now);
      supportRequest.setUpdatedDate(now);

      // Set the Athlete and Federation entities (Many-to-One relationships)
      supportRequest.setAthlete(athlete);

      // Step 4: Save the SupportRequest entity
      SupportRequest savedRequest = supportRequestRepository.save(supportRequest);

      // Step 5: Return the saved entity
      return savedRequest;
  }


  @Override
  public SupportRequest createSupportRequestFromFederation(SupportRequestDTO supportRequestDTO, Long federationId) {
      // Fetch the federation from the database
      Federation federation = federationRepository.findById(federationId)
              .orElseThrow(() -> new RuntimeException("Federation not found"));

      // Create a new SupportRequest entity from the DTO
      SupportRequest supportRequest = new SupportRequest();
      supportRequest.setSubject(supportRequestDTO.getSubject());
      supportRequest.setMontant(supportRequestDTO.getMontant());
      supportRequest.setJustification(supportRequestDTO.getJustification());
      supportRequest.setDescription(supportRequestDTO.getDescription());
      supportRequest.setPriority(supportRequestDTO.getPriority());
      supportRequest.setStatus("Pending Review"); // Set status to "Pending Review"
      supportRequest.setAttachments(supportRequestDTO.getAttachments());
      supportRequest.setCreatedDate(supportRequestDTO.getCreatedDate());
      supportRequest.setUpdatedDate(supportRequestDTO.getUpdatedDate());
      supportRequest.setFederation(federation);

      // Save the SupportRequest entity
      return supportRequestRepository.save(supportRequest);
  }
  @Override
  public SupportRequest updateSupportRequest(SupportRequest request) throws ServiceException {
    // Validate request data and existence
    if (request.getSupportRequestId() == null || !supportRequestRepository.existsById(request.getSupportRequestId())) {
      throw new ServiceException("Invalid request ID or request not found.");
    }

    // Update the request object
    SupportRequest existingRequest = supportRequestRepository.findById(request.getSupportRequestId()).get();
    existingRequest.setSubject(request.getSubject());
    existingRequest.setMontant(request.getMontant());
    existingRequest.setJustification(request.getJustification());
    existingRequest.setDescription(request.getDescription());
    existingRequest.setPriority(request.getPriority());
    existingRequest.setAttachments(request.getAttachments());

    // Save the updated request
    SupportRequest updatedRequest = supportRequestRepository.save(existingRequest);

    return updatedRequest;
  }
  @Override
  public SupportRequest getSupportRequestById(Long supportRequestId) {
    return supportRequestRepository.findById(supportRequestId).orElse(null);
  }
  @Override
  public List<SupportRequest> getAllSupportRequests(Optional<String> status) {
    if (status.isPresent()) {
      return supportRequestRepository.findByStatus(status.get());
    } else {
      return supportRequestRepository.findAll();
    }
  }
  @Override
  public List<SupportRequest> getSupportRequestsByAthlete(Long athleteId) {
      return supportRequestRepository.findByAthlete_AthleteId(athleteId);
  }
  @Override
  public List<SupportRequest> getAllSupportRequests() {
      return supportRequestRepository.findAll();
  }
  @Override
  public void deleteSupportRequest(Long requestId) throws Exception {
      SupportRequest request = getSupportRequestById(requestId);
      supportRequestRepository.delete(request);
  }
  @Override
  public void changeRequestStatus(Long supportRequestId, String newStatus) throws ServiceException {
    if (!supportRequestRepository.existsById(supportRequestId)) {
      throw new ServiceException("Invalid request ID or request not found.");
    }
    SupportRequest request = supportRequestRepository.findById(supportRequestId).get();
    request.setUpdatedDate(LocalDate.now());
    request.setStatus(newStatus);

    supportRequestRepository.save(request);
  }
  @Override
  public Athlete getAthleteById(Long athleteId) {
      return athleteRepository.findById(athleteId)
              .orElseThrow(() -> new RuntimeException("Athlete not found with id: " + athleteId));
  }

  @Override
  public Federation getFederationById(Long federationId) {
      return federationRepository.findById(federationId)
              .orElseThrow(() -> new RuntimeException("Federation not found with id: " + federationId));
  }
  
  @Override
  public List<SupportRequestDTO> getAllSupportRequestsDTO() {
      List<SupportRequest> requests = supportRequestRepository.findAll();
      List<SupportRequestDTO> requestDTOs = new ArrayList<>();

      for (SupportRequest request : requests) {
          // Map the SupportRequest entity to SupportRequestDTO
          SupportRequestDTO dto = new SupportRequestDTO();
          dto.setSupportRequestId(request.getSupportRequestId());
          dto.setSubject(request.getSubject());
          dto.setMontant(request.getMontant());
          dto.setJustification(request.getJustification());
          dto.setDescription(request.getDescription());
          dto.setPriority(request.getPriority());
          dto.setStatus(request.getStatus());
          dto.setAttachments(request.getAttachments());
          dto.setCreatedDate(request.getCreatedDate());
          dto.setUpdatedDate(request.getUpdatedDate());

          // Set Athlete and Federation IDs
          if (request.getAthlete() != null) {
              dto.setAthleteId(request.getAthlete().getAthleteId()); // Assuming getId() method
          }
          if (request.getFederation() != null) {
              dto.setFederationId(request.getFederation().getFederationId()); // Assuming getId() method
          }

          // Map support responses if needed
          List<SupportResponseDTO> responses = new ArrayList<>();
          for (SupportResponse response : request.getSupportResponses()) {
              SupportResponseDTO responseDTO = new SupportResponseDTO();
              responseDTO.setSupportResponseId(response.getSupportResponseId());
              responseDTO.setMessage(response.getMessage());
              responseDTO.setMontant(response.getMontant());
              responseDTO.setCreatedDate(response.getCreatedDate());
              responses.add(responseDTO);
          }
          dto.setSupportResponses(responses);

          // Add the DTO to the list
          requestDTOs.add(dto);
      }

      return requestDTOs;
  }
  
  
}