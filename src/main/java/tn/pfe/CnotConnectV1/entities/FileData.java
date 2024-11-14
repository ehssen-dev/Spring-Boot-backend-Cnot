package tn.pfe.CnotConnectV1.entities;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "file_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileId;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type")
    private String contentType;

    // Option 1: Store file content as a BLOB (consider limitations and security)
    @Lob
    @Basic(fetch = FetchType.LAZY) // Fetch lazily to avoid large BLOBs in memory
    private byte[] fileContent;

    // Option 2: Reference to a separate file storage location (recommended for large files)
    @Column(name = "file_path")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    private Document document;

}