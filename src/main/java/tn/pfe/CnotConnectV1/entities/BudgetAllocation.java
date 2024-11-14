package tn.pfe.CnotConnectV1.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.pfe.CnotConnectV1.enums.BudgetStatus;
import tn.pfe.CnotConnectV1.enums.Category;
@Entity
@Table(name = "budget_allocations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long budgetId;
    
    @Column(name = "budget_number", unique = true, nullable = false)
    private String budgetNumber;

    @Column(name = "allocated_amount")
    private Double allocatedAmount;

    @Column(name = "used_budget")
    private Double usedBudget = 0.0;

    @Column(name = "remaining_budget")
    private Double remainingBudget;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private BudgetStatus budgetStatus;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    @JsonIgnore
    private ProcurementRequest procurementRequest;

    @OneToMany(mappedBy = "budget")
    @JsonIgnore
    private List<Invoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_report_id")
    @JsonIgnore
    private FinancialReport financialReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @PostLoad
    public void updateRemainingBudget() {
        this.remainingBudget = this.allocatedAmount - this.usedBudget;
        if (this.remainingBudget < 0) {
            this.budgetStatus = BudgetStatus.IN_DANGER;
        } else {
            this.budgetStatus = BudgetStatus.NORMAL;
        }
    }
}
