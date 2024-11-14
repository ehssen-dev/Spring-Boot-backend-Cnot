package tn.pfe.CnotConnectV1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Document;


@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    //List<Document> findDocumentByProfildocIdProfil(Long idProfil);
   
	//List<Document> findDocumentByProfilId(Long idProfil);
	
   
}