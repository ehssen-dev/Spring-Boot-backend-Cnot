package tn.pfe.CnotConnectV1.entities;

import java.util.List;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "keywords")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long keywordId;

    @Column(name = "keyword")
    private String keyword;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "keywords")
    private List<Document> documents;

}