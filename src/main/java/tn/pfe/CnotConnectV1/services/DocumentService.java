package tn.pfe.CnotConnectV1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.pfe.CnotConnectV1.entities.Document;
import tn.pfe.CnotConnectV1.repository.DocumentRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IDocumentService;

@Slf4j
@Service
public class DocumentService implements IDocumentService {
    @Autowired
    DocumentRepository documentRepository;
    @Override
    public List<Document> retrieveAllDocuments() {
        return documentRepository.findAll();
    }



    @Override
    public Document updateDocument(Document d) {
       
            return documentRepository.save(d);
        
        }
    

    @Override
    public Document addDocument(Document d) {
        return documentRepository.save(d);
    }

    @Override
    public Document retrieveDocument(Long documentId) {
        return documentRepository.findById(documentId).orElse(null);
    }

    @Override
    public void removeDocument(Long documentId) {
        documentRepository.deleteById(documentId);

    }

   
}