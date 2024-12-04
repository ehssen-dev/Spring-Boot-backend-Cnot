package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gameId;

    private String name;
    private LocalDate date;
    private String location;
    private String description;
    @Column(name = "start_Game")
    private LocalDate startGame;
    @Column(name = "end_Game")
    private LocalDate endGame;
    
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonIgnoreProperties("games")  // Prevents infinite recursion
    private Event event;
   
   
    @OneToMany(mappedBy = "game")
    @JsonManagedReference
    private Set<Athlete> athletes = new HashSet<>();
    
    @OneToOne(mappedBy = "game", fetch = FetchType.LAZY)
    @JsonManagedReference  // Change from @JsonIgnore to @JsonManagedReference
    private Result result;
    
    @OneToOne
    @JoinColumn(name = "archive_id")
    @JsonIgnore
    private Archive archive;
    
    public void setArchive(Archive archive) {
        if (this.archive != null) {
            this.archive.setGame(null);
        }
        this.archive = archive;
        if (archive != null) {
            archive.setGame(this);
        }
    }
    
    public boolean hasEnded() {
        return LocalDate.now().isAfter(endGame);
    }
    
    
    /**
     * Adds an athlete to this game.
     *
     * @param athlete the athlete to add
     */
    public void addAthlete(Athlete athlete) {
        if (athlete == null) {
            throw new IllegalArgumentException("Athlete cannot be null.");
        }
        athletes.add(athlete); 
        athlete.setGame(this); 
    }
    
    /**
     * Removes an athlete from this game.
     *
     * @param athlete the athlete to remove
     */
    public void removeAthlete(Athlete athlete) {
        if (athlete != null && athletes.contains(athlete)) {
            athletes.remove(athlete); 
            athlete.setGame(null); 
        }
    }
 // Criteria for continuous tracking
    public boolean isReadyForResult() {
        return this.hasEnded() && result != null && result.getStatus() != null;
    }
    // Monitor game progress and check the status
    public void evaluateGameProgress() {
        if (hasEnded()) {
            if (isReadyForResult()) {
                System.out.println("Game " + this.name + " is complete and the result is ready.");
            } else {
                System.out.println("Game " + this.name + " ended, waiting for result.");
            }
        } else {
            System.out.println("Game " + this.name + " is ongoing.");
        }
    }
}
