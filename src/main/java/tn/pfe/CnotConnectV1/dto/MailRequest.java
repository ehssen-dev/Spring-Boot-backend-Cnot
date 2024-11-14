package tn.pfe.CnotConnectV1.dto;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import tn.pfe.CnotConnectV1.entities.Attachment;
import tn.pfe.CnotConnectV1.enums.Qualification;
@Getter
@Setter
public class MailRequest {
    private Long mailId;
    private String senderEmail; // New field for sender email
    private String recipient;
    private String subject;
    private String content;
    private String sentDate;
    private String receivedDate;
    private String processedDate;
    private String archivedDate;
    private Qualification qualificationType;
    private String type;
    private String status;

    private List<AttachmentResponseDTO> attachments;

    @Getter
    @Setter
    public static class AttachmentResponseDTO {
        private Long id;
        private String fileName;
        private String fileType;
        private byte[] data;
    }
}
