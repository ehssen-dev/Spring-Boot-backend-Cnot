package tn.pfe.CnotConnectV1.entities;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;
import tn.pfe.CnotConnectV1.enums.MailStatus;
import tn.pfe.CnotConnectV1.enums.MailType;
import tn.pfe.CnotConnectV1.enums.Qualification;

@Entity
@Table(name = "mails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mailId;

    @Column(nullable = false)
    private String sender; 

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000) 
    private String content;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime sentDate;
    
    private LocalDateTime receivedDate;
    @UpdateTimestamp
    private LocalDateTime processedDate;

    private LocalDateTime archivedDate;

    @Enumerated(EnumType.STRING)
    private Qualification qualificationType;

    @Enumerated(EnumType.STRING)
    private MailType type;

    @Enumerated(EnumType.STRING)
    private MailStatus status;
    
  /* 
    @Column(name = "attachment")
    private String attachment;*/

    @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "athlete_id")
    @JsonBackReference
    private Athlete athlete;

    @ManyToOne
    @JoinColumn(name = "delegation_id")
    private Delegation delegation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
   /* @Lob
    @Column(nullable = true)
    private byte[] messageBytes;

    public Message getMessage() throws MessagingException, IOException {
        return new MimeMessage(Session.getDefaultInstance(new Properties()), new ByteArrayInputStream(messageBytes));
    }

    public void setMessage(Message message) throws MessagingException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        messageBytes = outputStream.toByteArray();
    }*/
}
