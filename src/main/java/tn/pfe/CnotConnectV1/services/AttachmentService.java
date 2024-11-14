package tn.pfe.CnotConnectV1.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.Attachment;
import tn.pfe.CnotConnectV1.repository.AttachmentRepository;

@Service
public class AttachmentService {
	  @Autowired
	    private AttachmentRepository attachmentRepository;
	  
	  public InputStream getAttachmentById(String id) {
	        Attachment attachment = attachmentRepository.findById(Long.parseLong(id))
	                .orElseThrow(() -> new RuntimeException("Attachment not found"));
	        return new ByteArrayInputStream(attachment.getData());
	    }

	    public String getMimeTypeById(String id) {
	        Attachment attachment = attachmentRepository.findById(Long.parseLong(id))
	                .orElseThrow(() -> new RuntimeException("Attachment not found"));
	        return attachment.getFileType();
	    }
}