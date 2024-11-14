package tn.pfe.CnotConnectV1.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EntityEmailsDTO {
    private List<String> athleteEmails;
    private List<String> userEmails;
    private List<String> delegationEmails;
    
    
	public EntityEmailsDTO(List<String> athleteEmails, List<String> userEmails, List<String> delegationEmails) {
		super();
		this.athleteEmails = athleteEmails;
		this.userEmails = userEmails;
		this.delegationEmails = delegationEmails;
	}

    
}
