package tn.pfe.CnotConnectV1.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
	
	

	private Long departmentId;
	   @NotEmpty(message = "Name is required")
	    private String name;

	    @NotEmpty(message = "Email is required")
	    @Email(message = "Email should be valid")
	    private String email;

	    @NotEmpty(message = "Contact information is required")
	    private String contactInformation;

	    @NotEmpty(message = "Responsibilities are required")
	    private String responsibilities;
	    
	    public DepartmentDTO(Long departmentId2, String name2) {
			// TODO Auto-generated constructor stub
		}
}