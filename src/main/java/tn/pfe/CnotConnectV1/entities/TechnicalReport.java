package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table(name = "technical_reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tReportId;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(nullable = false)
    private String reportPeriod;

    @Column(nullable = false)
    private String reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
    
   
}