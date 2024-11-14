package tn.pfe.CnotConnectV1.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.pfe.CnotConnectV1.dto.EntityEmailsDTO;
import tn.pfe.CnotConnectV1.dto.MailRequest;
import tn.pfe.CnotConnectV1.entities.Mail;
import tn.pfe.CnotConnectV1.enums.MailStatus;
import tn.pfe.CnotConnectV1.enums.Qualification;

import tn.pfe.CnotConnectV1.services.interfaces.IMailService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/mails")
public class MailController {

    private final IMailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    public MailController(IMailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/all-emails")
    public ResponseEntity<EntityEmailsDTO> getAllEntityEmails() {
        EntityEmailsDTO entityEmails = mailService.getAllEntityEmails();
        return ResponseEntity.ok(entityEmails);
    }
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<Mail>> getMailsByAthleteId(@PathVariable Long athleteId) {
        try {
            List<Mail> mails = mailService.getMailsByAthleteId(athleteId);
            if (mails.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content if no emails found
            }
            return ResponseEntity.ok(mails); // 200 OK with the list of mails
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null); // 500 Internal Server Error if something goes wrong
        }
    }

    @GetMapping("/list-emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> allEmails = mailService.getAllEmails();
        return ResponseEntity.ok(allEmails);
    }

   
    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(
            @ModelAttribute @Valid MailRequest mailRequest,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        try {
            // Validate sender email
            if (mailRequest.getSenderEmail() == null || mailRequest.getSenderEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Sender email is required.");
            }

            // Validate recipient email
            if (mailRequest.getRecipient() == null || mailRequest.getRecipient().isEmpty()) {
                return ResponseEntity.badRequest().body("Recipient email is required.");
            }

            // Call service method to send the mail
            mailService.sendMailsV(mailRequest, files);
            return ResponseEntity.ok("Mail sent successfully.");
        } catch (Exception e) {
            logger.error("Error sending mail: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error sending mail: " + e.getMessage());
        }
    }

    @PostMapping("/send-to-all-entities")
    public ResponseEntity<String> sendMailToAllEntities(
            @ModelAttribute MailRequest mailRequest, 
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        try {
            // Call the service method with the updated parameters
            mailService.sendMailToAllEntities(mailRequest, files);
            return ResponseEntity.ok("Emails sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emails: " + e.getMessage());
        }
    }


    @PostMapping("/send-to-athletes")
    public ResponseEntity<String> sendMailToAllAthletes(
            @ModelAttribute MailRequest mailRequest, 
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        try {
            // Call the service method to send emails to all athletes with the provided mailRequest and files
            mailService.sendMailToAllAthletes(mailRequest, files);
            return ResponseEntity.ok("Emails sent to all athletes successfully.");
        } catch (Exception e) {
            // Return internal server error with the exception message if something goes wrong
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send emails to athletes: " + e.getMessage());
        }
    }






    @PostMapping("/send-to-delegations")
    public ResponseEntity<String> sendMailToAllDelegations(
            @RequestParam("senderEmail") String senderEmail,
            @ModelAttribute MailRequest mailRequest,
            @RequestParam(required = false) List<MultipartFile> files) {

        // Validate sender email
        if (senderEmail == null || senderEmail.isEmpty()) {
            return ResponseEntity.badRequest().body("Sender email address must not be null or empty.");
        }

        try {
            mailService.sendMailToAllDelegations(senderEmail, mailRequest, files);
            return ResponseEntity.ok("Emails sent to all delegations successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send emails to delegations: " + e.getMessage());
        }
    }

    @PostMapping("/send-to-users")
    public ResponseEntity<String> sendMailToAllUsers(
            @RequestParam("senderEmail") String senderEmail,
            @ModelAttribute MailRequest mailRequest,
            @RequestParam(required = false) List<MultipartFile> files) {

        // Validate sender email
        if (senderEmail == null || senderEmail.isEmpty()) {
            return ResponseEntity.badRequest().body("Sender email address must not be null or empty.");
        }

        try {
            mailService.sendMailToAllUsers(senderEmail, mailRequest, files);
            return ResponseEntity.ok("Emails sent to all users successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send emails to users: " + e.getMessage());
        }
    }


    @GetMapping("/search")
    public Page<Mail> searchMails(
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) LocalDateTime sentDate,
            @RequestParam(required = false) MailStatus status,
            Pageable pageable) {
        return mailService.searchMails(sender, recipient, subject, sentDate, status, pageable);
    }

    @PostMapping("/receive")
    public ResponseEntity<Void> receiveMail(@RequestBody Mail mail) {
        mailService.receiveMail(mail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{mailId}/qualify")
    public ResponseEntity<Void> qualifyAndDistributeMail(
            @PathVariable("mailId") Long mailId,
            @RequestParam("qualificationType") Qualification qualificationType) {
        mailService.qualifyAndDistributeMail(mailId, qualificationType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-em")
    public ResponseEntity<List<Mail>> getAllMails() {
        try {
            List<Mail> mailDTOs = mailService.getAllMails(); // Assuming mailService returns List<MailDTO>
            if (mailDTOs.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 No Content if empty
            }
            return ResponseEntity.ok(mailDTOs);
        } catch (Exception e) {
            // Handle exception, log it, and return a meaningful error response
            // For example, you might return a 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{mailId}")
    public ResponseEntity<Mail> getMailById(@PathVariable("mailId") Long mailId) {
        Mail mail = mailService.getMailById(mailId);
        return ResponseEntity.ok().body(mail);
    }

    @PostMapping("/{mailId}/archive")
    public ResponseEntity<Mail> archiveMail(@PathVariable("mailId") Long mailId) {
        Mail archivedMail = mailService.archiveMail(mailId);
        return ResponseEntity.ok().body(archivedMail);
    }
    @DeleteMapping("/delete/{mailId}")
    public ResponseEntity<Void> deleteMail(@PathVariable("mailId") Long mailId) {
        try {
            mailService.deleteMail(mailId); // Ensure this service method exists
            return ResponseEntity.noContent().build(); // Return 204 No Content if successful
        } catch (Exception e) {
            logger.error("Error deleting mail with ID {}: {}", mailId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}