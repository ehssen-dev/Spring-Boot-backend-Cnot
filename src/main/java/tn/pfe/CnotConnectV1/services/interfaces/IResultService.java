package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.ResultDTO;
import tn.pfe.CnotConnectV1.entities.Result;

public interface IResultService {
	
	//Result saveResult(Result result);
    
    Optional<Result> getResultById(Long resultId);
    
    List<Result> getAllResults();
    
    void deleteResult(Long resultId);

	Result saveResult(ResultDTO resultDTO);
}
