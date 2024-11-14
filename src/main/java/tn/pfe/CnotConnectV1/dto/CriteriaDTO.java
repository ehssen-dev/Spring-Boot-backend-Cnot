package tn.pfe.CnotConnectV1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.pfe.CnotConnectV1.entities.Criteria;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaDTO {
    private Long id;
    private String name;
    private String description;
    private String type; // Could be a specific type, e.g., "financial", "operational", etc.

    // Constructor to convert Criteria entity to DTO
    public CriteriaDTO(Criteria criteria) {
        this.id = criteria.getId();
        this.name = criteria.getName();
        this.description = criteria.getDescription();
        this.type = criteria.getType();
    }
}