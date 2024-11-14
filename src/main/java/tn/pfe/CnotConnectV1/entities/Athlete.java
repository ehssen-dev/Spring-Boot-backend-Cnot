package tn.pfe.CnotConnectV1.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tn.pfe.CnotConnectV1.enums.Gender;

@Entity
@Table(name = "athletes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Athlete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long athleteId;

    private String photo;

    @Column(name = "first_Name")
    private String firstName;

    @Column(name = "last_Name")
    private String lastName;

    @Column(name = "date_Of_Birth")
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private boolean bloque = false;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

   
    @Size(max = 20)
    private String username;

   
    @Size(max = 120)
    private String password;

    private String sport;

    private String city;

    private Integer phnum;

    private Integer cin;

    // Bidirectional relationship with Delegation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegation_id")
    @JsonIgnore
    private Delegation delegation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "federation_id")
    @JsonIgnore
    private Federation federation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    @OneToOne(mappedBy = "athlete", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    // One-to-many relationship with Document
    @OneToMany(mappedBy = "athlete", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Document> documents = new HashSet<>();

    // One-to-many relationship with PerformanceMetrics
    @OneToMany(mappedBy = "athlete", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PerformanceMetrics> performanceMetrics = new ArrayList<>();
    
    @OneToMany(mappedBy = "athlete")
    @JsonManagedReference
    private List<Mail> mails;
    // Constructor for basic attributes
    public Athlete(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Method to add PerformanceMetrics with linking
    public void addPerformanceMetrics(PerformanceMetrics metrics) {
        metrics.setAthlete(this); // Set the linking reference
        performanceMetrics.add(metrics);
    }
    
    public void joinFederation(Federation federation) {
        if (federation == null) {
            throw new IllegalArgumentException("Federation cannot be null.");
        }
        this.federation = federation; // Set the federation for the athlete
        federation.addAthlete(this); // Add the athlete to the federation's list
    }
    /**
     * Adds the athlete to a game.
     *
     * @param game the game to participate in
     */
    public void participateInGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        this.setGame(game); // Set the game for the athlete
        game.addAthlete(this); // Add the athlete to the game's list of athletes
    }
    
    /**
     * Removes the athlete from a game.
     */
    public void leaveGame() {
        if (this.game != null) {
            this.game.removeAthlete(this); // Remove the athlete from the current game's list of athletes
            this.game = null; // Clear the game reference for the athlete
        }
    }
    
}