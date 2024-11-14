package tn.pfe.CnotConnectV1.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FederationDTO {
    private Long federationId;
    private String federationNumber;
    private String name;
    private String description;
    private String email;
}