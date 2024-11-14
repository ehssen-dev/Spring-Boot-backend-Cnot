package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;
    private String name;
    @Column(name = "start_Event")
    private LocalDate startEvent;
    @Column(name = "end_Event")
    private LocalDate endEvent;
    private String description;
   
    @OneToMany(mappedBy = "event")
    @JsonManagedReference 
    private List<Game> games;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "archive_id")
    @JsonManagedReference
    private Archive archive;

    public void setArchive(Archive archive) {
        if (this.archive != null) {
            this.archive.setEvent(null);
        }
        this.archive = archive;
        if (archive != null) {
            archive.setEvent(this);
        }
    }
    
    public boolean hasEnded() {
        return LocalDate.now().isAfter(endEvent);
    }
   
    
}