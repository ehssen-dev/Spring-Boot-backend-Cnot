package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.pfe.CnotConnectV1.enums.ProjectStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

	  
     
      
		private Long projectId;
        private String projectName;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDate validationDate;
        private ProjectStatus status;
        private Boolean technicalReportSubmitted=false;
        private Boolean financialReportSubmitted=false;
        private Boolean includedInProgram;
        private Date reportSubmissionDate;
        
        private List<BudgetAllocationDTO> budgetAllocations;  
        private List<PurchaseOrderDTO> purchaseOrders; 
        private DepartmentDTO department; 
        private SolidarityOlympicDTO solidarityOlympic;
     //   private ArchiveDTO archive; 
      //  private UserDTO requestedBy; 
        
        private Long requestedById;
        private Long departmentId;
        private Long solidarityOlympicId;
      //  private Long archiveId; 
		public ProjectDTO(Long projectId, String projectName, String description, LocalDate startDate,
				LocalDate endDate, LocalDate validationDate, ProjectStatus status, Boolean technicalReportSubmitted,
				Boolean financialReportSubmitted, Boolean includedInProgram, Date reportSubmissionDate,
				Long departmentId, Long solidarityOlympicId) {
			super();
			this.projectId = projectId;
			this.projectName = projectName;
			this.description = description;
			this.startDate = startDate;
			this.endDate = endDate;
			this.validationDate = validationDate;
			this.status = status;
			this.technicalReportSubmitted = technicalReportSubmitted;
			this.financialReportSubmitted = financialReportSubmitted;
			this.includedInProgram = includedInProgram;
			this.reportSubmissionDate = reportSubmissionDate;
			this.departmentId = departmentId;
			this.solidarityOlympicId = solidarityOlympicId;
		}
        
        
        
		
	    
}
