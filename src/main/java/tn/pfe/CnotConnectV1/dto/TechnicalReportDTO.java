package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalReportDTO {
    private Long tReportId;
    private LocalDate reportDate;
    private String reportPeriod;
    private String reportType;
    private ProjectDTO project; 
    private Long projectId;
}
