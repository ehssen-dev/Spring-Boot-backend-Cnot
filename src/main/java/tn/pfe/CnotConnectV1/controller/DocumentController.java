package tn.pfe.CnotConnectV1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.entities.Document;
import tn.pfe.CnotConnectV1.services.interfaces.IDocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final IDocumentService documentService;

    @Autowired
    public DocumentController(IDocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.retrieveAllDocuments();
        return ResponseEntity.ok().body(documents);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<Document> getDocumentById(@PathVariable("documentId") Long documentId) {
        Document document = documentService.retrieveDocument(documentId);
        if (document != null) {
            return ResponseEntity.ok().body(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Document> addDocument(@RequestBody Document document) {
        Document createdDocument = documentService.addDocument(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Document> updateDocument(@PathVariable("documentId") Long documentId, @RequestBody Document document) {
        Document updatedDocument = documentService.retrieveDocument(documentId);
        if (updatedDocument != null) {
            updatedDocument.setContent(document.getContent()); // Update content or any other fields you want
            updatedDocument.setName(document.getName()); // Update title or any other fields you want
            updatedDocument = documentService.updateDocument(updatedDocument);
            return ResponseEntity.ok().body(updatedDocument);
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("documentId") Long documentId) {
        Document document = documentService.retrieveDocument(documentId);
        if (document != null) {
            documentService.removeDocument(documentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}