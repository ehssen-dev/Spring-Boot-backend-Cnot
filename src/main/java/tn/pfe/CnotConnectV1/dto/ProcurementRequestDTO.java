package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import tn.pfe.CnotConnectV1.enums.RequestStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcurementRequestDTO {
	

	public ProcurementRequestDTO(Long requestId, String requestNumber, String description, 
            LocalDateTime requestedDate, String userIdentifier,
            Double estimatedCost, String justification, String status, 
            LocalDate submissionDate, LocalDate approvalDate, 
            String requestedGoods, Integer quantity) {
this.requestId = requestId;
this.requestNumber = requestNumber;
this.description = description;
this.requestedDate = requestedDate;
this.userIdentifier = userIdentifier; // Expecting String identifier
this.estimatedCost = estimatedCost;
this.justification = justification;
this.status = status;
this.submissionDate = submissionDate;
this.approvalDate = approvalDate;
this.requestedGoods = requestedGoods;
this.quantity = quantity;
}

	private Long requestId;
    
    private  String requestNumber;
    @NotNull
    private String description;
    private LocalDateTime requestedDate;

    private String userIdentifier;

    @NotNull
    private Double estimatedCost;
    @NotNull
    private String justification;

    private String  status;

    private LocalDate submissionDate;

    private LocalDate approvalDate;

    private Long projectId; // Ensure this is set

    private ProjectDTO project; // Make sure this has a non-null ID

    @NotNull
    private String requestedGoods;

    @NotNull
    private Integer quantity;
}

