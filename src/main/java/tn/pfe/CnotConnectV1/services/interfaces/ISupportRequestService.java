package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;

import tn.pfe.CnotConnectV1.dto.SupportRequestDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Federation;
import tn.pfe.CnotConnectV1.entities.SupportRequest;

public interface ISupportRequestService {

    /**
     * Creates a support request from an athlete.
     *
     * @param athlete the athlete submitting the request
     * @param subject the subject of the request
     * @param description the description of the request
     * @return the created support request
     * @throws ServiceException if subject or description is empty
     */
   /* SupportRequest createSupportRequestFromAthlete(
    		Athlete athlete,
    		String subject,
    		String description,  
    		Double montant, 
    	    int priority, 
    	    String justification, 
    	    byte[] attachments) throws ServiceException;*/

    /**
     * Creates a support request from a federation.
     *
     * @param federation the federation submitting the request
     * @param subject the subject of the request
     * @param description the description of the request
     * @return the created support request
     * @throws ServiceException if subject or description is empty
     */
    //SupportRequest createSupportRequestFromFederation(Federation federation, String subject, String description) throws ServiceException;

    /**
     * Updates an existing support request.
     *
     * @param request the updated support request data
     * @return the updated support request
     * @throws ServiceException if request ID is invalid or request not found
     */
    SupportRequest updateSupportRequest(SupportRequest request) throws ServiceException;

    /**
     * Gets a support request by ID.
     *
     * @param id the ID of the request to retrieve
     * @return the support request with the given ID, or null if not found
     */
    SupportRequest getSupportRequestById(Long supportRequestId);

    /**
     * Gets all support requests with optional filtering by status.
     *
     * @param status (optional) the status to filter by
     * @return a list of support requests, optionally filtered by status
     */
    List<SupportRequest> getAllSupportRequests(Optional<String> status);

    /**
     * Gets all support requests submitted by a specific athlete.
     *
     * @param athlete_id the ID of the athlete to filter by
     * @return a list of support requests submitted by the athlete
     */
    List<SupportRequest> getSupportRequestsByAthlete(Long athleteId);

    /**
     * Deletes a support request by ID.
     *
     * @param requestId the ID of the request to delete
     * @throws Exception if the request cannot be deleted
     */
    void deleteSupportRequest(Long supportRequestId) throws Exception;

    /**
     * Changes the status of a support request.
     *
     * @param id the ID of the request to update
     * @param newStatus the new status for the request
     * @throws ServiceException if request ID is invalid or request not found
     */
    void changeRequestStatus(Long supportRequestId, String newStatus) throws ServiceException;

	List<SupportRequest> getAllSupportRequests();

	Athlete getAthleteById(Long athleteId);

	Federation getFederationById(Long federationId);

	SupportRequest createSupportRequestFromAthlete(SupportRequestDTO supportRequestDTO, Long athleteId);

	SupportRequest createSupportRequestFromFederation(SupportRequestDTO supportRequestDTO, Long federationId);

	List<SupportRequestDTO> getAllSupportRequestsDTO();
}