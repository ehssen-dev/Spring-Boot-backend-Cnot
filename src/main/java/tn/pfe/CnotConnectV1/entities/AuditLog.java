package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mailId; 

    @Column(nullable = false)
    private String action;  // "CREATED", "UPDATED", "DELETED"

    @Column(nullable = false)
    private String performedBy; 

    @Column(nullable = false)
    private LocalDateTime timestamp; 

    @Column
    private String details;

  
}