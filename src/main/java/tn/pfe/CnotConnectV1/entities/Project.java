package tn.pfe.CnotConnectV1.entities;

import java.io.Serializable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import tn.pfe.CnotConnectV1.enums.ProjectStatus;


@Entity
@Table(name = "projects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @Column(name = "project_name")
    private String projectName;

    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "validation_date")
    private LocalDate validationDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "technical_report_submitted")
    private Boolean technicalReportSubmitted = false;

    @Column(name = "financial_report_submitted")
    private Boolean financialReportSubmitted = false;

    @Column(name = "included_in_program")
    private Boolean includedInProgram;

    @Column(name = "report_submission_date")
    private Date reportSubmissionDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference // This annotation helps with bidirectional relationships
    private Department department;

    @ManyToOne
    @JoinColumn(name = "solidarity_olympic_id")
    @JsonBackReference
    private SolidarityOlympic solidarityOlympic;

  

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TechnicalReport> technicalReports = new ArrayList<>();;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FinancialReport> financialReport = new ArrayList<>();;

  

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BudgetAllocation> budgetAllocations = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JsonManagedReference
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "archive_id")
    @JsonIgnore
    private Archive archive;

    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcurementRequest> procurementRequests = new ArrayList<>();
    
    
    public void setArchive(Archive archive) {
        if (this.archive != null) {
            this.archive.setProject(null);
        }
        this.archive = archive;
        if (archive != null) {
            archive.setProject(this);
        }
    }
}
