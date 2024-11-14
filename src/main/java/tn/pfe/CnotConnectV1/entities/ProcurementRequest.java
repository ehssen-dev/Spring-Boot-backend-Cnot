package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import tn.pfe.CnotConnectV1.enums.RequestStatus;


@Entity
@Table(name = "procurement_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcurementRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;
    private  String requestNumber;
    @Column( name = "requested_Date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestedDate;

    @Column(name = "requested_Goods", nullable = false)
    private String requestedGoods;
    
    @NotNull
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requested_by", nullable = false) 
    @JsonBackReference
    private User requestedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "estimated_cost", nullable = false)
    private Double estimatedCost;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BudgetAllocation> budgets = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference // Ensure this annotation is used properly
    private Project project;
}