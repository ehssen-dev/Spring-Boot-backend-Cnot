package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.dto.DelegationDTO;
import tn.pfe.CnotConnectV1.entities.Delegation;


public interface IDelegationService {

    public List<Delegation> retrieveAllDelegations();

	Delegation getDelegationById(Long delegationId);

	//Delegation createDelegation(Delegation delegation);

	//Delegation updateDelegation(Long delegationId, Delegation updatedDelegation);

	void deleteDelegation(Long delegationId);

	DelegationDTO updateDelegation(Long delegationId, DelegationDTO delegationDTO);

	DelegationDTO createDelegation(DelegationDTO delegationDTO);

}
