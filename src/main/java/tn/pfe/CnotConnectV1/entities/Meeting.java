package tn.pfe.CnotConnectV1.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "meetings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long meetingId;

    @Column(name = "meeting_date")
    @Temporal(TemporalType.TIMESTAMP) // Include time for specific meeting times
    private Date meetingDate;

    @Column(name = "meeting_topic")
    private String meetingTopic;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private List<Participant> participants; // Optional: List of participants

   

    // ... Getters, Setters, and other methods
}