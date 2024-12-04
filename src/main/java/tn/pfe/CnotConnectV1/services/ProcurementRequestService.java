package tn.pfe.CnotConnectV1.services;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.dto.UserDTO;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.enums.RequestStatus;
import tn.pfe.CnotConnectV1.exeptions.NotFoundException;
import tn.pfe.CnotConnectV1.exeptions.ProcurementRequestNotFoundException;
import tn.pfe.CnotConnectV1.repository.ProcurementRequestRepository;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;
import tn.pfe.CnotConnectV1.repository.UserRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IProcurementRequestService;

@Service
public class ProcurementRequestService implements IProcurementRequestService{

	@Autowired
    private ProcurementRequestRepository procurementRequestRepository;
	@Autowired
    private ProjectRepository projectRepository;
	@Autowired
    private UserRepository userRepository;
	

	@Override
	@Transactional
	public ProcurementRequest createProcurementRequest(ProcurementRequestDTO dto) {
	    if (dto == null) {
	        throw new IllegalArgumentException("DTO must not be null");
	    }

	    if (dto.getUserIdentifier() == null || dto.getUserIdentifier().isEmpty()) {
	        throw new IllegalArgumentException("User identifier must be provided");
	    }
	    if (dto.getRequestedGoods() == null || dto.getRequestedGoods().isEmpty()) {
	        throw new IllegalArgumentException("Requested goods must be provided");
	    }
	    if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
	        throw new IllegalArgumentException("Quantity must be a positive integer");
	    }
	    if (dto.getEstimatedCost() == null || dto.getEstimatedCost() <= 0) {
	        throw new IllegalArgumentException("Estimated cost must be a positive value");
	    }

	    // Step 1: Fetch the user based on the provided identifier in the DTO
	    User requestedBy = userRepository.findByIdentifier(dto.getUserIdentifier())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid user identifier"));

	    // Step 2: Generate a request number
	    String requestNumber = generateRequestNumber();

	    // Step 3: Create and populate the procurement request entity
	    ProcurementRequest procurementRequest = new ProcurementRequest();
	    procurementRequest.setRequestNumber(requestNumber);
	    procurementRequest.setRequestedDate(LocalDateTime.now());
	    procurementRequest.setSubmissionDate(LocalDate.now());
	    procurementRequest.setRequestedBy(requestedBy);
	    procurementRequest.setEstimatedCost(dto.getEstimatedCost());
	    procurementRequest.setRequestedGoods(dto.getRequestedGoods());
	    procurementRequest.setQuantity(dto.getQuantity());
	    procurementRequest.setJustification(dto.getJustification());
	    procurementRequest.setDescription(dto.getDescription());
	    procurementRequest.setStatus(RequestStatus.PENDING);

	    return procurementRequestRepository.save(procurementRequest);
	}




	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 7; 

    private String generateRequestNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);
        
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return sb.insert(LENGTH / 2, '-').toString();
    }


    // Validate the input procurement request
  /*  private void validateProcurementRequest(ProcurementRequest procurementRequest) {
       
        if (procurementRequest == null) {
            throw new IllegalArgumentException("Procurement request cannot be null");
        }

        if (procurementRequest.getRequestedBy() == null) {
            throw new IllegalArgumentException("Requested by field is required");
        }}*/

      

    
	@Override
	public ProcurementRequest findProcurementRequestById(Long requestId) {
	    return procurementRequestRepository.findById(requestId)
	            .orElseThrow(() -> new ProcurementRequestNotFoundException("ProcurementRequest with id " + requestId + " not found"));
	}

	@Override
	@Transactional
	public ProcurementRequest updateProcurementRequest(Long requestId, ProcurementRequestDTO updatedRequest) {
	    if (requestId == null) {
	        throw new IllegalArgumentException("The given id must not be null!");
	    }

	    ProcurementRequest existingRequest = procurementRequestRepository.findById(requestId)
	            .orElseThrow(() -> new NoSuchElementException("Procurement request not found"));

	    if (updatedRequest.getRequestedDate() != null) {
	        existingRequest.setRequestedDate(updatedRequest.getRequestedDate());
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        if (existingRequest.getRequestedDate().isBefore(currentDateTime)) {
	            throw new IllegalArgumentException("Requested date cannot be in the past");
	        }
	    }
	    
	    if (updatedRequest.getRequestedGoods() != null) {
	        existingRequest.setRequestedGoods(updatedRequest.getRequestedGoods());
	    }

	    if (updatedRequest.getQuantity() != null) {
	        existingRequest.setQuantity(updatedRequest.getQuantity());
	    }

	    if (updatedRequest.getDescription() != null) {
	        existingRequest.setDescription(updatedRequest.getDescription());
	    }

	    if (updatedRequest.getEstimatedCost() != null) {
	        existingRequest.setEstimatedCost(updatedRequest.getEstimatedCost());
	    }

	    if (updatedRequest.getJustification() != null) {
	        existingRequest.setJustification(updatedRequest.getJustification());
	    }

	    if (updatedRequest.getSubmissionDate() != null) {
	        existingRequest.setSubmissionDate(updatedRequest.getSubmissionDate());
	    }

	    if (updatedRequest.getApprovalDate() != null) {
	        existingRequest.setApprovalDate(updatedRequest.getApprovalDate());
	    }

	    if (updatedRequest.getProjectId() != null) {
	        Project project = projectRepository.findById(updatedRequest.getProjectId())
	                .orElseThrow(() -> new NoSuchElementException("Project not found"));
	        existingRequest.setProject(project);
	    }

	    return procurementRequestRepository.save(existingRequest);
	}


	@Override
	@Transactional
	public List<ProcurementRequestDTO> getAllProcurementRequests() {
	    List<ProcurementRequest> procurementRequests = procurementRequestRepository.findAll();
	    return procurementRequests.stream()
	        .map(this::mapToDTO)
	        .collect(Collectors.toList());
	}
    @Override
    public void deleteProcurementRequest(Long requestId) {
        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException("Invalid requestId");
        }
        if (!procurementRequestRepository.existsById(requestId)) {
            throw new NoSuchElementException("Procurement request not found");
        }
        procurementRequestRepository.deleteById(requestId);
    }
    
    @Override
    public List<ProcurementRequest> getPendingRequests() {
        
        return procurementRequestRepository.findByStatus(RequestStatus.PENDING);
    }
    
    @Override
    public List<ProcurementRequestDTO> getPendingRequestsDTO() {
        List<ProcurementRequest> pendingRequests = procurementRequestRepository.findByStatus(RequestStatus.PENDING);
        return pendingRequests.stream()
                              .map(request -> convertToDTO(request))
                              .collect(Collectors.toList());
    }

    private ProcurementRequestDTO convertToDTO(ProcurementRequest request) {
        ProcurementRequestDTO dto = new ProcurementRequestDTO();
        dto.setRequestId(request.getRequestId());
        dto.setDescription(request.getDescription());
        dto.setQuantity(request.getQuantity());
        dto.setRequestedGoods(request.getRequestedGoods());
      
        return dto;
    }
    @Override
    public ProcurementRequest updateRequestStatus(Long requestId, RequestStatus status) {
        ProcurementRequest request = procurementRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Procurement request not found with id: " + requestId));
        
        request.setStatus(status);
        
        return procurementRequestRepository.save(request);
    }
    
    @Transactional
    @Override
    public void processRequest(Long requestId, boolean approved, String comments) {
        ProcurementRequest request = procurementRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        switch (request.getStatus()) {
            case PENDING:
                submitRequestForApproval(request);
                break;
            case DEPARTMENT_HEAD:
            case FINANCE:
                if (approved) {
                    approveStage(request, comments);
                } else {
                    rejectRequest(request, comments);
                }
                break;
            case APPROVED:
            case REJECTED:
                throw new RuntimeException("Request has already been processed.");
            default:
                throw new RuntimeException("Invalid request status.");
        }
    }


    @Transactional
    @Override
    public void submitRequestForApproval(ProcurementRequest request) {
        if (request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.DEPARTMENT_HEAD);
            procurementRequestRepository.save(request);
        } else {
            throw new RuntimeException("Request is already in process or has been finalized.");
        }
    }

    @Transactional
    @Override
    public void approveStage(ProcurementRequest request, String comments) {
        RequestStatus currentStatus = request.getStatus();

        if (currentStatus == RequestStatus.DEPARTMENT_HEAD) {
            request.setStatus(RequestStatus.FINANCE);
        } else if (currentStatus == RequestStatus.FINANCE || currentStatus == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.APPROVED);
        } else {
            throw new RuntimeException("Invalid status for approval or request already processed.");
        }
        request.setApprovalDate(LocalDate.now());
        procurementRequestRepository.save(request);
    }

    @Transactional
    @Override
    public void rejectRequest(ProcurementRequest request, String comments) {
        RequestStatus currentStatus = request.getStatus();

        if (currentStatus == RequestStatus.DEPARTMENT_HEAD || currentStatus == RequestStatus.FINANCE || currentStatus == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.REJECTED);
            // request.setRejectionReason(comments);
        } else {
            throw new RuntimeException("Request cannot be rejected at this stage.");
        }
        request.setApprovalDate(LocalDate.now());

        procurementRequestRepository.save(request);
    }

    @Transactional
    @Override
    public void resetRequest(Long requestId) {
        ProcurementRequest request = procurementRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() == RequestStatus.REJECTED || request.getStatus() == RequestStatus.APPROVED) {
            request.setStatus(RequestStatus.PENDING);
            request.setApprovalDate(null);
        } else {
            throw new RuntimeException("Request cannot be reset at this stage.");
        }

        procurementRequestRepository.save(request);
    }
	 
    
    @Override
    public ProcurementRequestDTO mapToDTO(ProcurementRequest request) {
        return new ProcurementRequestDTO(
            request.getRequestId(),
            request.getRequestNumber(),
            request.getDescription(),
            request.getRequestedDate(),
            request.getRequestedBy() != null ? request.getRequestedBy().getIdentifier() : null, // Use identifier
            request.getEstimatedCost(),
            request.getJustification(),
            request.getStatus().toString(), 
            request.getSubmissionDate(),
            request.getApprovalDate(),
            request.getRequestedGoods(),
            request.getQuantity()
        );
    }


 /*
    private UserDTO mapUserToDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getIdentifier());
    }
*/
    
    
}