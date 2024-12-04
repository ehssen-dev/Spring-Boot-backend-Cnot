package tn.pfe.CnotConnectV1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDataDTO {
    private Long processId;
    private Long criteriaId;
    private BigDecimal value; 
    private LocalDateTime timestamp;
}