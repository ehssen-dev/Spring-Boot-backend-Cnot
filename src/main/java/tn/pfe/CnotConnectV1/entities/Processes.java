package tn.pfe.CnotConnectV1.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;
import tn.pfe.CnotConnectV1.dto.PerformanceDataDTO;


@Entity
@Table(name = "processes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Processes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "process_criteria",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "criteria_id")
    )
    private Set<Criteria> criteria = new HashSet<>();

    @Column(nullable = true)
    private BigDecimal kpiValue;

    @Transient
    private final Logger logger = LoggerFactory.getLogger(Process.class);
    
    // Method to add a Criteria to the Process
    public void addCriteria(Criteria criteria) {
        this.criteria.add(criteria);
    }

    public void removeCriteria(Criteria criteria) {
        this.criteria.remove(criteria);
    }
    

    // Method to track and update performance metrics
    public void trackPerformance(PerformanceDataDTO performanceData) {
      
        logger.info("Tracking performance for process {} with criteria {}: value = {}",
                this.name, performanceData.getCriteriaId(), performanceData.getValue());

        updateKPI(performanceData.getValue());

       
        if (isCriticalPerformance(performanceData.getValue())) {
            logger.warn("Critical performance level detected for process {}: value = {}",
                    this.name, performanceData.getValue());
           
        }
    }

    // Helper method to update KPI value
    private void updateKPI(BigDecimal value) {
        this.kpiValue = value;
       
    }

    // Helper method to check for critical performance levels
    private boolean isCriticalPerformance(BigDecimal value) {
        BigDecimal criticalThreshold = new BigDecimal("1000"); 
        return value.compareTo(criticalThreshold) > 0;
    }
    
}