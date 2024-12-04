package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.pfe.CnotConnectV1.enums.Gender;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AthleteDTO {
    private Long athleteId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String sport;
    private String city; 
    private Gender gender;

    public AthleteDTO(Long athleteId, String firstName, String lastName, LocalDate dateOfBirth, String city, Gender gender, String sport) {
        this.athleteId = athleteId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.gender = gender;
        this.sport = sport;
    }
    public AthleteDTO(Long athleteId, String firstName, String lastName, LocalDate dateOfBirth, String email, String sport) {
        this.athleteId = athleteId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.sport = sport;
    }

}
