package tn.pfe.CnotConnectV1.entities;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "performance_metrics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long performanceId;

    @Column(name = "gold_medals")
    private int goldMedals;

    @Column(name = "silver_medals")
    private int silverMedals;

    @Column(name = "bronze_medals")
    private int bronzeMedals;
    
    @Column(name = "total_medals")
    private int totalMedals;
    @Column(name = "total_Games")
    private int totalGames;
    
    @Column(name = "tournament_participation")
    private int tournamentParticipation;
    
    @Column(name = "adherence_to_regulations")
    private boolean adherenceToRegulations;
    
    @Column(name = "average_finish_time")
    private double averageFinishTime;
    
    @ManyToOne
    @JoinColumn(name = "athlete_id")
    @JsonBackReference
    private Athlete athlete;
   
    
    
    public int getTotalMedals() {
        return goldMedals + silverMedals + bronzeMedals;
    }

	
    
}
