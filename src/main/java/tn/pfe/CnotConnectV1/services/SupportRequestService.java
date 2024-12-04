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
 
      Athlete athlete = athleteRepository.findById(athleteId)
              .orElseThrow(() -> new RuntimeException("Athlete not found"));

    
      SupportRequest supportRequest = new SupportRequest();
      supportRequest.setSubject(supportRequestDTO.getSubject());
      supportRequest.setMontant(supportRequestDTO.getMontant());
      supportRequest.setJustification(supportRequestDTO.getJustification());
      supportRequest.setDescription(supportRequestDTO.getDescription());
      supportRequest.setPriority(supportRequestDTO.getPriority());
      supportRequest.setStatus("Pending Review"); 
      supportRequest.setAttachments(supportRequestDTO.getAttachments());

      // No attachments now, so just skip this part

    
      LocalDate now = LocalDate.now();
      supportRequest.setCreatedDate(now);
      supportRequest.setUpdatedDate(now);

      supportRequest.setAthlete(athlete);

      SupportRequest savedRequest = supportRequestRepository.save(supportRequest);

      return savedRequest;
  }


  @Override
  public SupportRequest createSupportRequestFromFederation(SupportRequestDTO supportRequestDTO, Long federationId) {
   
      Federation federation = federationRepository.findById(federationId)
              .orElseThrow(() -> new RuntimeException("Federation not found"));

      SupportRequest supportRequest = new SupportRequest();
      supportRequest.setSubject(supportRequestDTO.getSubject());
      supportRequest.setMontant(supportRequestDTO.getMontant());
      supportRequest.setJustification(supportRequestDTO.getJustification());
      supportRequest.setDescription(supportRequestDTO.getDescription());
      supportRequest.setPriority(supportRequestDTO.getPriority());
      supportRequest.setStatus("Pending Review"); 
      supportRequest.setAttachments(supportRequestDTO.getAttachments());
      supportRequest.setCreatedDate(supportRequestDTO.getCreatedDate());
      supportRequest.setUpdatedDate(supportRequestDTO.getUpdatedDate());
      supportRequest.setFederation(federation);

      return supportRequestRepository.save(supportRequest);
  }
  @Override
  public SupportRequest updateSupportRequest(SupportRequest request) throws ServiceException {
    if (request.getSupportRequestId() == null || !supportRequestRepository.existsById(request.getSupportRequestId())) {
      throw new ServiceException("Invalid request ID or request not found.");
    }

    SupportRequest existingRequest = supportRequestRepository.findById(request.getSupportRequestId()).get();
    existingRequest.setSubject(request.getSubject());
    existingRequest.setMontant(request.getMontant());
    existingRequest.setJustification(request.getJustification());
    existingRequest.setDescription(request.getDescription());
    existingRequest.setPriority(request.getPriority());
    existingRequest.setAttachments(request.getAttachments());

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

          if (request.getAthlete() != null) {
              dto.setAthleteId(request.getAthlete().getAthleteId()); 
          }
          if (request.getFederation() != null) {
              dto.setFederationId(request.getFederation().getFederationId()); 
          }

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

          requestDTOs.add(dto);
      }

      return requestDTOs;
  }
  
  
}