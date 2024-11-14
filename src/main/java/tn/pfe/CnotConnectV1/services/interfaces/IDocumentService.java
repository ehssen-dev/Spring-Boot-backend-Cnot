package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.entities.Document;

public interface IDocumentService {
    public List<Document> retrieveAllDocuments();

    public Document updateDocument (Document d );

    public  Document addDocument(Document d);

    public Document retrieveDocument (Long  documentId);

    public  void removeDocument(Long documentId);
}