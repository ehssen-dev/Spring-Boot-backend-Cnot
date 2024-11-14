package tn.pfe.CnotConnectV1.entities;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "Courriers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Courrier {




	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courrierId;

    private String type; // digital or physical
    private String source; // email, paper, pdf, etc.
    private String status; // acquired, qualified, distributed, etc.
    private Date dateReceived;
    private String subject;
    private String content;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

	public Courrier( String type, String source, String status, Date dateReceived , Department department, String subject,
			String content) {
		super();
		this.type = type;
		this.source = source;
		this.status = status;
		this.dateReceived = dateReceived;
		this.department = department;
		this.subject = subject;
		this.content = content;
		
	}
    
    

    /*public Courrier(String type, String source, String status, Date dateReceived, Department department, User sender, User recipient, String subject, String content) {
        this.type = type;
        this.source = source;
        this.status = status;
        this.dateReceived = dateReceived;
        this.department = department;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
    }*/

	/*public Courrier(String type, String source, String status, Date dateReceived, String subject, String content,
			Department department) {
		
		this.type = type;
		this.source = source;
		this.status = status;
		this.dateReceived = dateReceived;
		this.department = department;
		this.subject = subject;
		this.content = content;
	}*/
    
   
}