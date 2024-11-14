package tn.pfe.CnotConnectV1.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tn.pfe.CnotConnectV1.enums.TypeDocument;


@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long documentId;
    private String name;
    @Column(name = "content_Type")
    private String contentType;  // (e.g., application/pdf, text/plain)
    @Lob
    private byte[] content;
    @Column(name = "upload_Date")
    private Date uploadDate;
    @Enumerated(EnumType.STRING)
    private TypeDocument typedocument;
    private Boolean valeur;
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;
    
    @ManyToOne
    @JoinColumn(name = "athlete_id")
    @JsonBackReference
    private Athlete athlete;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "document_keywords",
                joinColumns = @JoinColumn(name = "document_id"),
                inverseJoinColumns = @JoinColumn(name = "keyword_id"))
    private List<Keyword> keywords;
    
    /*@OneToOne
    @JoinColumn(name = "project_id")
    private Project project;*/
    
    @OneToOne // Document owns the meeting (minutes)
    @JoinColumn(name = "meeting_id") 
    private Meeting meeting;
    

  /* @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id")
    private User uploader;*/
    
}