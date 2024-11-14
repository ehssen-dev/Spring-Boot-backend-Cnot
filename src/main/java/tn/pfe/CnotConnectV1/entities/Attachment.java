package tn.pfe.CnotConnectV1.entities;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.*;
import tn.pfe.CnotConnectV1.dto.AttachmentSerializer;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = AttachmentSerializer.class)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_id", nullable = false)
    private Mail mail;
}