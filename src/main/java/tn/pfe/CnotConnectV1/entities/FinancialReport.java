package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "financial_reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FinancialReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fReportId;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date reportDate;

    @Column(nullable = false)
    private String reportPeriod;

    @Column(nullable = false)
    private String reportType;

    @Column(name = "total_income")
    private Double totalIncome;

    @Column(name = "total_expenditure")
    private Double totalExpenditure;

    @Column(name = "net_income")
    private Double netIncome;

   
    @OneToMany(mappedBy = "financialReport", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "financialReport", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BudgetAllocation> budgets = new ArrayList<>();
    
   
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore

    private Project project;
    
}
