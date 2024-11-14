package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.dto.SupportResponseDTO;
import tn.pfe.CnotConnectV1.entities.SupportResponse;

public interface ISupportResponseService {

	  /**
	   * Adds a support response to a support request.
	   *
	   * @param requestId the ID of the support request
	   * @param response the support response data
	   * @return the saved support response
	   * @throws Exception if the support request is not found or there's an error saving the response
	   */
	  SupportResponse addSupportResponse(Long supportRequestId, SupportResponse response) throws Exception;

	  /**
	   * Gets all support responses associated with a support request.
	   *
	   * @param requestId the ID of the support request
	   * @return a list of support responses for the request
	   * @throws Exception if the support request is not found
	   */
	  List<SupportResponse> getSupportResponsesByRequest(Long supportRequestId) throws Exception;

	  /**
	   * Gets a support response by ID.
	   *
	   * @param responseId the ID of the response to retrieve
	   * @return the support response with the given ID
	   * @throws Exception if the support response is not found
	   */
	  SupportResponse getSupportResponseById(Long supportResponseId) throws Exception;

	  /**
	   * Updates an existing support response.
	   *
	   * @param response the updated support response data
	   * @return the updated support response
	   * @throws Exception if the support response is not found or there's an error saving the update
	   */
	  SupportResponse updateSupportResponse(SupportResponse response) throws Exception;

	  /**
	   * Deletes a support response by ID.
	   *
	   * @param responseId the ID of the response to delete
	   * @throws Exception if the support response is not found or there's an error deleting it
	   */
	  void deleteSupportResponse(Long supportResponseId) throws Exception;

	SupportResponseDTO createSupportResponse(SupportResponseDTO supportResponseDTO);

	//List<SupportResponse> getAllSupportResponses();

	List<SupportResponseDTO> getAllSupportResponsesDTO();
	}