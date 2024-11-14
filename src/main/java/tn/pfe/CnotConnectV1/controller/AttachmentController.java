package tn.pfe.CnotConnectV1.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.services.AttachmentService;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getAttachmentById(@PathVariable("id") String id) {
        try {
            InputStream attachmentStream = attachmentService.getAttachmentById(id);
            String mimeType = attachmentService.getMimeTypeById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, mimeType);
            headers.add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            return new ResponseEntity<>(new InputStreamResource(attachmentStream), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}