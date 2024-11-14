package tn.pfe.CnotConnectV1.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.SupportResponseDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.SupportRequest;
import tn.pfe.CnotConnectV1.entities.SupportResponse;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.SupportRequestRepository;
import tn.pfe.CnotConnectV1.repository.SupportResponseRepository;
import tn.pfe.CnotConnectV1.repository.UserRepository;
import tn.pfe.CnotConnectV1.services.interfaces.ISupportResponseService;

@Service
public class SupportResponseService  implements ISupportResponseService{

  @Autowired
  private SupportRequestRepository supportRequestRepository;

  @Autowired
  private UserRepository userRepository; 

  @Autowired
  private SupportResponseRepository supportResponseRepository;
  @Autowired
  private AthleteRepository athleteRepository;
  

  @Autowired
  private SupportRequestService supportRequestService; 

@Override
  public SupportResponse addSupportResponse(Long supportRequestId, SupportResponse response) throws Exception {
	    SupportRequest request = supportRequestService.getSupportRequestById(supportRequestId);
	    response.setSupportRequest(request); 
	    request.addSupportResponse(response);
	    supportResponseRepository.save(response); 
	    return response;
	}
@Override
public SupportResponseDTO createSupportResponse(SupportResponseDTO supportResponseDTO) {
    SupportResponse supportResponse = new SupportResponse();
    supportResponse.setMessage(supportResponseDTO.getMessage());
    supportResponse.setMontant(supportResponseDTO.getMontant());

    // Decode each Base64 attachment string to byte[] and collect into a list
    if (supportResponseDTO.getAttachments() != null) {
        List<byte[]> decodedAttachments = supportResponseDTO.getAttachments().stream()
            .map(Base64.getDecoder()::decode)
            .collect(Collectors.toList());
        supportResponse.setAttachments(decodedAttachments); // Assuming supportResponse can handle List<byte[]>
    }

    supportResponse.setCreatedDate(supportResponseDTO.getCreatedDate());

    // Retrieve the associated SupportRequest
    SupportRequest supportRequest = supportRequestRepository.findById(supportResponseDTO.getSupportRequestId())
        .orElseThrow(() -> new RuntimeException("SupportRequest not found"));
    supportResponse.setSupportRequest(supportRequest);

    // Retrieve the associated Athlete if provided
    if (supportResponseDTO.getAthleteId() != null) {
        Athlete athlete = athleteRepository.findById(supportResponseDTO.getAthleteId())
            .orElseThrow(() -> new RuntimeException("Athlete not found"));
        supportResponse.setAthlete(athlete);
    }

    SupportResponse savedResponse = supportResponseRepository.save(supportResponse);

    // Convert the saved entity back to a DTO
    supportResponseDTO.setSupportResponseId(savedResponse.getSupportResponseId());
    return supportResponseDTO;
}

/*@Override
public List<SupportResponse> getAllSupportResponses() {
    return supportResponseRepository.findAll();
}
*/
@Override
  public List<SupportResponse> getSupportResponsesByRequest(Long supportRequestId) throws Exception {
      SupportRequest request = supportRequestService.getSupportRequestById(supportRequestId); 
      return request.getSupportResponses();
  }
@Override
  public SupportResponse getSupportResponseById(Long supportResponseId) throws Exception {
      SupportResponse response = supportResponseRepository.findById(supportResponseId)
              .orElseThrow(() -> new Exception("Support Response not found with id: " + supportResponseId));
      return response;
  }
@Override
  public SupportResponse updateSupportResponse(SupportResponse response) throws Exception {
	  Long supportResponseId = response.getSupportResponseId();
	    SupportResponse existingResponse = getSupportResponseById(supportResponseId);

      // Update relevant fields (avoid overwriting everything)
      existingResponse.setMessage(response.getMessage());
      existingResponse.setMontant(response.getMontant());
      existingResponse.setAttachments(response.getAttachments());

      // Save the updated response
      return supportResponseRepository.save(existingResponse);
  }
@Override
  public void deleteSupportResponse(Long supportResponseId) throws Exception {
      SupportResponse response = getSupportResponseById(supportResponseId);
      supportResponseRepository.delete(response);
  }
  

@Override
public List<SupportResponseDTO> getAllSupportResponsesDTO() {
    // Fetch all support responses from the repository
    List<SupportResponse> responses = supportResponseRepository.findAll();
    List<SupportResponseDTO> responseDTOs = new ArrayList<>();

    for (SupportResponse response : responses) {
        // Create a new DTO object
        SupportResponseDTO dto = new SupportResponseDTO();
        dto.setSupportResponseId(response.getSupportResponseId());
        dto.setMessage(response.getMessage());
        dto.setMontant(response.getMontant());
        dto.setCreatedDate(response.getCreatedDate());

        // Set SupportRequest ID if available
        if (response.getSupportRequest() != null) {
            dto.setSupportRequestId(response.getSupportRequest().getSupportRequestId());
        }

        // Set Athlete ID if available
        if (response.getAthlete() != null) {
            dto.setAthleteId(response.getAthlete().getAthleteId());
        }

        // Handle attachments - convert byte[] to Base64 if available
        if (response.getAttachments() != null && !response.getAttachments().isEmpty()) {
            List<String> base64Attachments = response.getAttachments().stream()
                .map(attachment -> Base64.getEncoder().encodeToString(attachment)) // Convert byte[] to Base64
                .collect(Collectors.toList());
            dto.setAttachments(base64Attachments);
        } else {
            // Set to an empty list if attachments are null or empty
            dto.setAttachments(Collections.emptyList());
        }

        // Add the DTO to the list
        responseDTOs.add(dto);
    }

    return responseDTOs; // Return the list of DTOs
}


}
