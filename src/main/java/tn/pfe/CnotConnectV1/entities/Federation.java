package tn.pfe.CnotConnectV1.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "federations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Federation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long federationId;
    @Column(name = "federation_Number")
    private String federationNumber;
    
    private String name;

    private String description;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @OneToMany(mappedBy = "delegation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Athlete> athletes = new ArrayList<>();
  
    public void addAthlete(Athlete athlete) {
        if (athlete != null) {
            athletes.add(athlete); // Add the athlete to the list
            athlete.setFederation(this); // Set the federation in the athlete
        }
    }
    
}