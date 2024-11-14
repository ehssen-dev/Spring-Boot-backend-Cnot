package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "archives")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Archive {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long archiveId;
	private String name;
    @Column(name = "date")
    private LocalDate date;
    private String description;
  
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
 
    
    
    @OneToOne(mappedBy = "archive")
    @JsonBackReference
    private Event event;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "result_id")
    @JsonIgnore
    private Result result;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;
   
}
