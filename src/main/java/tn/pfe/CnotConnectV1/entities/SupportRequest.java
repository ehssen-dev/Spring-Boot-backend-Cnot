package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Entity
@Table(name = "support_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long supportRequestId;


  @Column(name = "subject")
  private String subject;
  
  @Column(name = "montant")
  private Double montant;
  @Column(name = "justification")
  private String justification;
  @Column(name = "description")
  private String description;
  @Column(name = "priority")
  private int priority; // (e.g., 1 - High, 2 - Medium, 3 - Low)

  @Column(name = "status")
  private String status; // (e.g., "Open", "Pending Review", "Resolved")
 
  @Lob 
  @Column(name = "attachments")
  private byte[] attachments; 

  @Column(name = "created_date")
  private LocalDate createdDate;

  @Column(name = "updated_date")
  private LocalDate updatedDate;
  
  @OneToMany(mappedBy = "supportRequest", cascade = CascadeType.ALL)
  @JsonIgnoreProperties("supportRequest") 
  private List<SupportResponse> supportResponses = new ArrayList<>();


  @ManyToOne 
  @JoinColumn(name = "athlete_id")
  @JsonIgnoreProperties("supportRequest") 
  private Athlete athlete;

  @ManyToOne
  @JoinColumn(name = "federation_id")
  @JsonIgnoreProperties("supportRequest") 
  private Federation federation;

  
  public void addSupportResponse(SupportResponse response) {
      response.setSupportRequest(this); 
      supportResponses.add(response);
  }
  
}