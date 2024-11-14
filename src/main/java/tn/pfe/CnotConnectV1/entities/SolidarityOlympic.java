package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@Table(name = "solidarity_olympic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolidarityOlympic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long solidarityOlympicId;
    
    @Column(name = "program_name")
    private String programName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    
    @OneToMany(mappedBy = "solidarityOlympic")
    @JsonManagedReference
    private List<Project> projects = new ArrayList<>();
    
    public void addProject(Project project) {
        project.setSolidarityOlympic(this);
        projects.add(project);
    }
    
    public void removeProject(Project project) {
        projects.remove(project);
        project.setSolidarityOlympic(null);
    }
}