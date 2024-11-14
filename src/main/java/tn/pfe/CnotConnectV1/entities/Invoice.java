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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tn.pfe.CnotConnectV1.enums.InvoiceStatus;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Invoice  {
 
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long invoiceId;
    @Column(name = "invoice_Nmber")
    private String invoiceNumber;
    @Column(name = "total_Amount")
    private Double totalAmount;
    @Column(name = "invoice_Date")
    private Date invoiceDate;
    @Column(name = "due_Date")
    private Date dueDate;
    @Column(name = "is_paid")
    private boolean paid;
    private String description;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    @JsonIgnore
    private BudgetAllocation budget;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_report_id")
    @JsonIgnore
    private FinancialReport financialReport;
  
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
}