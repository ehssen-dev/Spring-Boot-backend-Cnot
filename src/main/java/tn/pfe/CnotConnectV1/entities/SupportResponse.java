package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table(name = "support_responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportResponse {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long supportResponseId;


  @Column(name = "message")
  private String message;
  @Column(name = "montant")
  private Double montant;
  @Lob 
  @Column(name = "attachments")
  @ElementCollection
  private List<byte[]> attachments; 

  @Column(name = "created_date")
  private LocalDate createdDate = LocalDate.now();
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "support_request_id")
  @JsonIgnore 
  private SupportRequest supportRequest;

  @ManyToOne(optional = true)
  private Athlete athlete;

  public void setSupportRequest(SupportRequest request) {
      this.supportRequest = request;
  }
}