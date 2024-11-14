package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.enums.RequestStatus;

public interface IProcurementRequestService {


	//ProcurementRequest getProcurementRequestById(Long requestId);

	void deleteProcurementRequest(Long requestId);


	List<ProcurementRequest> getPendingRequests();

	ProcurementRequest updateRequestStatus(Long requestId, RequestStatus status);

	ProcurementRequest createProcurementRequest(ProcurementRequestDTO dto);

	//List<ProcurementRequest> getAllProcurementRequests();



	ProcurementRequest updateProcurementRequest(Long requestId, ProcurementRequestDTO updatedRequestDTO);

	ProcurementRequest findProcurementRequestById(Long requestId);


	List<ProcurementRequestDTO> getPendingRequestsDTO();

/////////////////////////
	
	
	
	void resetRequest(Long requestId);


	void rejectRequest(ProcurementRequest request, String comments);


	void approveStage(ProcurementRequest request, String comments);


	void submitRequestForApproval(ProcurementRequest request);


	void processRequest(Long requestId, boolean approved, String comments);


	ProcurementRequestDTO mapToDTO(ProcurementRequest request);


	List<ProcurementRequestDTO> getAllProcurementRequests();


	

}
