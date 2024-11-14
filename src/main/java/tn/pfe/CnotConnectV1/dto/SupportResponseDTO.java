package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportResponseDTO {

    private Long supportResponseId;
    private String message;
    private Double montant;
    private List<String> attachments;
    private LocalDate createdDate = LocalDate.now();
    private Long supportRequestId;
    private Long athleteId;

    
    
}
