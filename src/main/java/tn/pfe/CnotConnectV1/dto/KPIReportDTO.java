package tn.pfe.CnotConnectV1.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KPIReportDTO {
    private Long criteriaId;
    private String criteriaName;
    private Date startDate;
    private Date endDate;
    private BigDecimal kpiValue; 
    private String comments;
}