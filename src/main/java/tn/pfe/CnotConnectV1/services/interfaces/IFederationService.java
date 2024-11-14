package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.FederationDTO;
import tn.pfe.CnotConnectV1.entities.Federation;

public interface IFederationService {

	List<Federation> getAllFederations();

	Optional<Federation> getFederationById(Long federationId);

	//Federation saveFederation(Federation federation);

	void deleteFederation(Long federationId);

	FederationDTO saveFederation(FederationDTO federationDTO);

}
