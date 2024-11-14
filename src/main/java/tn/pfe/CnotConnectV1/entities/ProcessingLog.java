package tn.pfe.CnotConnectV1.entities;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Processing-log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingLog {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mailId;
    private String stage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}