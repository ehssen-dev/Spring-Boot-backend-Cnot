package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportRequestDTO {

    private Long supportRequestId;
    private String subject;
    private Double montant;
    private String justification;
    private String description;
    private int priority; // (e.g., 1 - High, 2 - Medium, 3 - Low)
    private String status; // (e.g., "Open", "Pending Review", "Resolved")
    private byte[] attachments; 
    private LocalDate createdDate;
    private LocalDate updatedDate;
    
    private Long athleteId;
    private Long federationId;
    private List<SupportResponseDTO> supportResponses; 
}