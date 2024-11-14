package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDateTime;
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
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resultId;
    private String resultNumber;

    private String winner;
    private String runnerUp;
    private String thirdPlace;
    private String scores;
    private String highlights;
    @Column(name = "status")
    private String status;
    @Column(name = "result_date")
    private LocalDateTime resultDate;
    @Column(name = "comments")
    private String comments;
    @Column(name = "archived")
    private Boolean archived = false;
  
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "archive_id")
    private Archive archive;
    
    public void setArchive(Archive archive) {
        if (this.archive != null) {
            this.archive.setResult(null);
        }
        this.archive = archive;
        if (archive != null) {
            archive.setResult(this);
        }
    }
    
    
   
    
    
}
