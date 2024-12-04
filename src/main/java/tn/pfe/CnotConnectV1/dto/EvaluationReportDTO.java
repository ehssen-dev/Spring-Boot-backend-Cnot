package tn.pfe.CnotConnectV1.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationReportDTO {
    private Long processId;
    private String processName;
    private Date startDate;
    private Date endDate;
    private String summary; 
    private String details; 
}
