package tn.pfe.CnotConnectV1.entities;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "participants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    // Optional: Additional attributes for attendance details
    @Column(name = "attendance_status") // e.g., "present", "absent", "excused"
    private String attendanceStatus;

    @Column(name = "notes")
    private String notes; // Optional: Notes about the participant's attendance or contribution

}