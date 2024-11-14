package tn.pfe.CnotConnectV1.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "delegations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delegation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long delegationId;

    @Column(name = "delegation_name")
    private String delegationName;

    private String country;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    // Bidirectional relationship with Athlete
    @OneToMany(mappedBy = "delegation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Athlete> athletes = new ArrayList<>();
    
    @OneToMany(mappedBy = "delegation")
    private List<Mail> mails;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    
    // Optional: Helper method to manage the relationship
    public void addAthlete(Athlete athlete) {
        athlete.setDelegation(this);
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete athlete) {
        athletes.remove(athlete);
        athlete.setDelegation(null);
    }
}