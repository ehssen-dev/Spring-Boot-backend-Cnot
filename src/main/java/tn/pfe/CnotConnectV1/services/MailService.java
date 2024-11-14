package tn.pfe.CnotConnectV1.services;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tn.pfe.CnotConnectV1.configuration.services.UserDetailsServiceImpl;
import tn.pfe.CnotConnectV1.dto.EntityEmailsDTO;
import tn.pfe.CnotConnectV1.dto.MailRequest;
import tn.pfe.CnotConnectV1.dto.MailRequest.AttachmentResponseDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Attachment;
import tn.pfe.CnotConnectV1.entities.AuditLog;
import tn.pfe.CnotConnectV1.entities.Courrier;
import tn.pfe.CnotConnectV1.entities.Delegation;
import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.entities.Document;
import tn.pfe.CnotConnectV1.entities.Mail;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.enums.MailStatus;
import tn.pfe.CnotConnectV1.enums.Qualification;
import tn.pfe.CnotConnectV1.exeptions.MailException;
import tn.pfe.CnotConnectV1.exeptions.MailNotFoundException;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.AttachmentRepository;
import tn.pfe.CnotConnectV1.repository.AuditLogRepository;
import tn.pfe.CnotConnectV1.repository.CourrierRepository;
import tn.pfe.CnotConnectV1.repository.DelegationRepository;
import tn.pfe.CnotConnectV1.repository.DepartmentRepository;
import tn.pfe.CnotConnectV1.repository.MailRepository;
import tn.pfe.CnotConnectV1.repository.UserRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IMailService;

@Service
@Transactional
public class MailService implements IMailService {
    @Autowired
    private  JavaMailSender javaMailSender;
    
    
    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private AthleteRepository athleteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private DelegationRepository delegationRepository;
    @Autowired
    private CourrierRepository courrierRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private AthleteService athleteService;
    @Autowired
    private DelegationService delegationService;
   @Autowired
    private UserDetailsServiceImpl userService;
   
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    
    @Value("${mail.imap.host}")
    private String mailHost;

    @Value("${mail.imap.port}")
    private String mailPort;

    @Value("${mail.imap.user}")
    private String mailUser;

    @Value("${mail.imap.password}")
    private String mailPassword;
    
   
    @Override
    public List<Mail> getAllMails() {
        return mailRepository.findAll();
    }
   

    @Override
    public Mail getMailById(Long mailId) {
        return mailRepository.findById(mailId).orElseThrow(() -> new RuntimeException("Mail not found with id: " + mailId));
    }
    @Override
    public List<Mail> getMailsByAthleteId(Long athleteId) {
        return mailRepository.findByAthleteAthleteId(athleteId);
    }
    //get all the emails from athlete , delegation and user
    @Override
    public EntityEmailsDTO getAllEntityEmails() {
        List<String> athleteEmails = athleteRepository.findAll().stream()
                                      .map(Athlete::getEmail)
                                      .collect(Collectors.toList());

        List<String> userEmails = userRepository.findAll().stream()
                                 .map(User::getEmail)
                                 .collect(Collectors.toList());

        List<String> delegationEmails = delegationRepository.findAll().stream()
                                           .map(Delegation::getEmail) // Assuming your Delegation entity has an email field
                                           .collect(Collectors.toList());

        return new EntityEmailsDTO(athleteEmails, userEmails, delegationEmails);
    }
    
    //get a list of all athlete , delegation and user
    @Override
    public List<String> getAllEmails() {
        List<String> athleteEmails = athleteRepository.findAll().stream()
                                      .map(Athlete::getEmail)
                                      .collect(Collectors.toList());

        List<String> userEmails = userRepository.findAll().stream()
                                 .map(User::getEmail)
                                 .collect(Collectors.toList());

        List<String> delegationEmails = delegationRepository.findAll().stream()
                                           .map(Delegation::getEmail)
                                           .collect(Collectors.toList());

        // Combine all the emails into one list
        List<String> allEmails = new ArrayList<>();
        allEmails.addAll(athleteEmails);
        allEmails.addAll(userEmails);
        allEmails.addAll(delegationEmails);

        return allEmails;
    }
    // send mails to a multiple or single recipient
    @Override
    @Transactional
    public void sendMailsV(MailRequest mailRequest, List<MultipartFile> files) throws IOException, MessagingException {
        // Create and save the mail entity
        Mail mail = new Mail();
        mail.setSender(mailRequest.getSenderEmail());
        mail.setRecipient(mailRequest.getRecipient());
        mail.setSubject(mailRequest.getSubject());
        mail.setContent(mailRequest.getContent());
        mail.setQualificationType(mailRequest.getQualificationType());
        mail.setStatus(MailStatus.NEW);

        // Check if the recipient is an athlete
        athleteRepository.findByEmail(mailRequest.getRecipient())
                         .ifPresent(mail::setAthlete);

        Mail savedMail = mailRepository.save(mail);

        // Handle file attachments
        saveAttachments(files, savedMail);

        // Send the email
        sendEmailWithAttachments(mailRequest, files);

        // Create and save audit log
        createAndSaveAuditLog(savedMail, mailRequest.getSenderEmail(), mailRequest.getRecipient());
    }

    // Helper method to handle saving attachments
    private void saveAttachments(List<MultipartFile> files, Mail savedMail) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            try {
                Attachment attachment = new Attachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileType(file.getContentType());
                attachment.setData(file.getBytes());
                attachment.setMail(savedMail);
                attachmentRepository.save(attachment);
                savedMail.getAttachments().add(attachment);
            } catch (IOException e) {
                logger.error("Error saving attachment: {}", e.getMessage(), e);
            }
        }
    }

    // Helper method to send email with attachments
    private void sendEmailWithAttachments(MailRequest mailRequest, List<MultipartFile> files) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(mailRequest.getSenderEmail());
        helper.setTo(mailRequest.getRecipient());
        helper.setSubject(mailRequest.getSubject());
        helper.setText(mailRequest.getContent(), true);

        if (files != null) {
            for (MultipartFile file : files) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }
        }

        javaMailSender.send(mimeMessage);
    }

    // Helper method to create and save audit log
    private void createAndSaveAuditLog(Mail savedMail, String senderEmail, String recipientEmail) {
        AuditLog auditLog = new AuditLog();
        auditLog.setMailId(savedMail.getMailId().toString());
        auditLog.setAction("SENT");
        auditLog.setPerformedBy("Action BY " + senderEmail);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails("Mail sent to " + recipientEmail);

        auditLogRepository.save(auditLog);
    }


    
    // send mails to a multiple (Bulk Email Sending)
    @Override
    public void sendMailToAllEntities(MailRequest mailRequest, List<MultipartFile> files) {
        List<String> recipientEmails = getAllEmails();

        if (recipientEmails.isEmpty()) {
            System.out.println("No recipient emails found.");
            return;
        }

        // Get the sender email directly from the mailRequest
        String senderEmail = mailRequest.getSenderEmail(); // Get sender email from the request

        for (String recipientEmail : recipientEmails) {
            try {
                // Prepare the email with attachments
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setFrom(senderEmail); // Set the sender email from the request
                helper.setTo(recipientEmail);
                helper.setSubject(mailRequest.getSubject());
                helper.setText(mailRequest.getContent(), true); // true indicates HTML content

                // Attach files if any
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        helper.addAttachment(file.getOriginalFilename(), file);
                    }
                }

                javaMailSender.send(mimeMessage);
                System.out.println("Email sent to " + recipientEmail + " successfully.");

                // Save mail entry in the database
                Mail mail = new Mail();
                mail.setSender(senderEmail); // Set the sender email from the request
                mail.setRecipient(recipientEmail);
                mail.setSubject(mailRequest.getSubject());
                mail.setContent(mailRequest.getContent());
                mail.setQualificationType(mailRequest.getQualificationType()); // Set the qualification type
                mail.setStatus(MailStatus.NEW);
                Mail savedMail = mailRepository.save(mail);

                // Handle and save attachments
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        Attachment attachment = new Attachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFileType(file.getContentType());
                        attachment.setData(file.getBytes());
                        attachment.setMail(savedMail);
                        attachmentRepository.save(attachment);
                        savedMail.getAttachments().add(attachment);
                    }
                    // Update the mail entity with attachments
                    mailRepository.save(savedMail);
                }

                // Create and save audit log
                AuditLog auditLog = new AuditLog();
                auditLog.setMailId(savedMail.getMailId().toString());
                auditLog.setAction("SENT");
                auditLog.setPerformedBy("Action BY " + senderEmail);
                auditLog.setTimestamp(LocalDateTime.now());
                auditLog.setDetails("Mail sent to " + recipientEmail);
                auditLogRepository.save(auditLog);

            } catch (Exception e) {
                System.err.println("Failed to send email to " + recipientEmail + ": " + e.getMessage());
            }
        }
    }



    
    // send mails to all athletes
    @Transactional
    @Override
    public void sendMailToAllAthletes(MailRequest mailRequest, List<MultipartFile> files) {
        // Get all athlete emails
        List<String> athleteEmails = athleteService.getAllEmails(); 

        if (athleteEmails.isEmpty()) {
            System.out.println("No athlete emails found.");
            return;
        }

        // Get the sender email directly from the mailRequest
        String senderEmail = mailRequest.getSenderEmail(); // Get sender email from the request
        if (senderEmail == null || senderEmail.isEmpty()) {
            throw new IllegalArgumentException("Sender email address must not be null or empty.");
        }

        // Loop through each athlete's email to send them the mail
        for (String recipientEmail : athleteEmails) {
            try {
                // Prepare the email with attachments
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                // Set the sender and recipient
                helper.setFrom(senderEmail);
                helper.setTo(recipientEmail);
                helper.setSubject(mailRequest.getSubject());
                helper.setText(mailRequest.getContent(), true); // true indicates HTML content

                // Attach files if any
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        helper.addAttachment(file.getOriginalFilename(), file);
                    }
                }

                // Send the email
                javaMailSender.send(mimeMessage);
                System.out.println("Email sent to " + recipientEmail + " successfully.");

                // Save the mail entry in the database
                Mail mail = new Mail();
                mail.setSender(senderEmail);
                mail.setRecipient(recipientEmail);
                mail.setSubject(mailRequest.getSubject());
                mail.setContent(mailRequest.getContent());
                mail.setQualificationType(mailRequest.getQualificationType());
                mail.setStatus(MailStatus.NEW);

                // Save the mail and obtain the savedMail reference
                Mail savedMail = mailRepository.save(mail);

                // Handle and save attachments
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        Attachment attachment = new Attachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFileType(file.getContentType());
                        attachment.setData(file.getBytes());
                        attachment.setMail(savedMail);
                        attachmentRepository.save(attachment);
                        savedMail.getAttachments().add(attachment);
                    }
                    // Update the mail entity with attachments
                    mailRepository.save(savedMail);
                }

                // Create and save audit log
                AuditLog auditLog = new AuditLog();
                auditLog.setMailId(savedMail.getMailId().toString());
                auditLog.setAction("SENT");
                auditLog.setPerformedBy("Action BY " + senderEmail);
                auditLog.setTimestamp(LocalDateTime.now());
                auditLog.setDetails("Mail sent to " + recipientEmail);
                auditLogRepository.save(auditLog);

            } catch (Exception e) {
                System.err.println("Failed to send email to " + recipientEmail + ": " + e.getMessage());
            }
        }
    }




    // send mails to all the delegations
    @Override
    public void sendMailToAllDelegations(String senderEmail, MailRequest mailRequest, List<MultipartFile> files) throws IOException {
        if (senderEmail == null || senderEmail.isEmpty()) {
            throw new IllegalArgumentException("Sender email address must not be null or empty.");
        }

        List<String> delegationEmails = delegationService.getAllEmails();

        if (delegationEmails.isEmpty()) {
            System.out.println("No delegation emails found.");
            return;
        }

        for (String recipientEmail : delegationEmails) {
            // Create a new Mail entity for each recipient
            Mail mail = new Mail();
            mail.setSender(senderEmail);
            mail.setRecipient(recipientEmail);
            mail.setSubject(mailRequest.getSubject());
            mail.setContent(mailRequest.getContent());
            mail.setQualificationType(mailRequest.getQualificationType());
            mail.setStatus(MailStatus.NEW);

            // Save the mail entity first
            Mail savedMail = mailRepository.save(mail);

            // Prepare and send email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(senderEmail);
                helper.setTo(recipientEmail);
                helper.setSubject(mailRequest.getSubject());
                helper.setText(mailRequest.getContent(), true); // true indicates HTML content

                // Attach files if provided
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        helper.addAttachment(file.getOriginalFilename(), file);
                    }
                }

                // Send email
                javaMailSender.send(mimeMessage);
                System.out.println("Email sent to " + recipientEmail + " successfully.");

                // Save attachments
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        try {
                            Attachment attachment = new Attachment();
                            attachment.setFileName(file.getOriginalFilename());
                            attachment.setFileType(file.getContentType());
                            attachment.setData(file.getBytes());
                            attachment.setMail(savedMail);
                            attachmentRepository.save(attachment);
                            savedMail.getAttachments().add(attachment);
                        } catch (IOException e) {
                            System.out.println("Error saving attachment: " + e.getMessage());
                        }
                    }
                    // Update mail entity with attachments
                    mailRepository.save(savedMail);
                }

                // Create and save audit log
                AuditLog auditLog = new AuditLog();
                auditLog.setMailId(savedMail.getMailId().toString());
                auditLog.setAction("SENT");
                auditLog.setPerformedBy("Action BY " + senderEmail);
                auditLog.setTimestamp(LocalDateTime.now());
                auditLog.setDetails("Mail sent to " + recipientEmail);
                auditLogRepository.save(auditLog);

            } catch (MessagingException e) {
                System.err.println("Failed to send email to " + recipientEmail + ": " + e.getMessage());
            }
        }
    }



    // send mails to all users
    @Override
    public void sendMailToAllUsers(String senderEmail, MailRequest mailRequest, List<MultipartFile> files) throws IOException {
        if (senderEmail == null || senderEmail.isEmpty()) {
            throw new IllegalArgumentException("Sender email address must not be null or empty.");
        }

        List<String> userEmails = userService.getAllEmails();

        if (userEmails.isEmpty()) {
            System.out.println("No user emails found.");
            return;
        }

        for (String recipientEmail : userEmails) {
            // Create a new Mail entity for each recipient
            Mail mail = new Mail();
            mail.setSender(senderEmail);
            mail.setRecipient(recipientEmail);
            mail.setSubject(mailRequest.getSubject());
            mail.setContent(mailRequest.getContent());
            mail.setQualificationType(mailRequest.getQualificationType());
            mail.setStatus(MailStatus.NEW);

            // Save the mail entity first
            Mail savedMail = mailRepository.save(mail);

            // Prepare and send email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(senderEmail);
                helper.setTo(recipientEmail);
                helper.setSubject(mailRequest.getSubject());
                helper.setText(mailRequest.getContent(), true); // true indicates HTML content

                // Attach files if provided
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        helper.addAttachment(file.getOriginalFilename(), file);
                    }
                }

                // Send email
                javaMailSender.send(mimeMessage);
                System.out.println("Email sent to " + recipientEmail + " successfully.");

                // Save attachments
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        try {
                            Attachment attachment = new Attachment();
                            attachment.setFileName(file.getOriginalFilename());
                            attachment.setFileType(file.getContentType());
                            attachment.setData(file.getBytes());
                            attachment.setMail(savedMail);
                            attachmentRepository.save(attachment);
                            savedMail.getAttachments().add(attachment);
                        } catch (IOException e) {
                            System.out.println("Error saving attachment: " + e.getMessage());
                        }
                    }
                    // Update mail entity with attachments
                    mailRepository.save(savedMail);
                }

                // Create and save audit log
                AuditLog auditLog = new AuditLog();
                auditLog.setMailId(savedMail.getMailId().toString());
                auditLog.setAction("SENT");
                auditLog.setPerformedBy("Action BY " + senderEmail);
                auditLog.setTimestamp(LocalDateTime.now());
                auditLog.setDetails("Mail sent to " + recipientEmail);
                auditLogRepository.save(auditLog);

            } catch (MessagingException e) {
                System.err.println("Failed to send email to " + recipientEmail + ": " + e.getMessage());
            }
        }
    }

    
    @Override
    public Page<Mail> searchMails(String sender, String recipient, String subject, LocalDateTime sentDate, MailStatus status, Pageable pageable) {
        Specification<Mail> spec = MailSpecification.byCriteria(sender, recipient, subject, sentDate , status);
        return mailRepository.findAll(spec, pageable);
    }



    ///////////////////////////////////
    public void fetchEmails() throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore("imaps");

        try {
            store.connect(mailHost, mailUser, mailPassword);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();

            for (Message message : messages) {
                MimeMessage mimeMessage = (MimeMessage) message;

                String subject = mimeMessage.getSubject();
                String content = getTextFromMessage(mimeMessage);

                // Example of dynamic department retrieval based on subject or content
                Department department = determineDepartment(subject, content);

                Courrier courrier = new Courrier(
                        "digital",
                        "email",
                        "acquired",
                        new Date(),
                        department,
                        subject,
                        content
                );

                courrierRepository.save(courrier);
                logger.info("Saved courrier from email with subject: {}", subject);
            }

            emailFolder.close(false);
        } catch (MessagingException | IOException e) {
            logger.error("Error fetching emails: {}", e.getMessage(), e);
            throw e;
        } finally {
            store.close();
        }
    }

    private Department determineDepartment(String subject, String content) {
        // Implement logic to determine department based on subject or content
        // This is a placeholder for demonstration purposes
        return departmentRepository.findById(1L).orElse(null);
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    private User findOrCreateUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email.split("@")[0]);
            return userRepository.save(newUser);
        });
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //////////////
    // 1. Acquisition du courrier
    @Override
    public void receiveMail(Mail mail) {
        Objects.requireNonNull(mail, "Mail object cannot be null");
        Objects.requireNonNull(mail.getSender(), "Sender of the mail cannot be null");
        Objects.requireNonNull(mail.getRecipient(), "Recipient of the mail cannot be null");

        LocalDateTime receivedDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        mail.setReceivedDate(receivedDateTime);
        mail.setStatus(MailStatus.NEW);
        mailRepository.save(mail);

        // Log the reception of a new mail
        System.out.println("Received new mail from " + mail.getSender());
    }

    // 2. Processus courrier (Qualification et distribution)    
    /*@Override
    public void qualifyAndDistributeMail(Long mailId, Qualification qualificationType) {
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new MailNotFoundException("Mail not found"));
        mail.setQualificationType(qualificationType);
        mail.setStatus(MailStatus.QUALIFIED);
        mailRepository.save(mail);

        distributeMail(mail);
    }
    private void distributeMail(Mail mail) {
        User recipient = mail.getRecipient();
        Department department = mail.getDepartment();
        
        // Notify the recipient or department (assuming method availability)
        sendNotificationEmail(recipient, mail);
        // Additional logic for distribution can be added here
    }*/
    @Override
    public void qualifyAndDistributeMail(Long mailId, Qualification qualificationType) {
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new MailNotFoundException("Mail not found"));
        mail.setQualificationType(qualificationType);
        mail.setStatus(MailStatus.NEW);
        mailRepository.save(mail);

        distributeMail(mail);
    }

    private void distributeMail(Mail mail) {
    	sendNotificationEmail(mail.getRecipient(), "New Mail Notification", 
    	        "You have received a new mail with subject: " + mail.getSubject());
    }

    // 3. Stockage et consultation (Classement structuré, indexation, consultation)
   

    @Transactional
    @Override
    public void sendMail(Mail mail) {
        // Set mail status (optional, could be done outside the method)
        mail.setStatus(MailStatus.NEW);
        
        // Validate recipient email address
        String recipientEmail = mail.getRecipient();
        if (!isValidEmail(recipientEmail)) {
            throw new MailException("Invalid recipient email address: " + recipientEmail);
        }

        // Create a MimeMessage for more control over email content
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipientEmail);
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
        };

        try {
            // Send the email
            javaMailSender.send(preparator);
            // Save the Mail entity (potentially after successful send)
            mailRepository.save(mail);
        } catch (Exception e) {
            // Handle any exception that occurs during sending or saving
            throw new MailException("Failed to send email: " + e.getMessage(), e);
        }
    }


    // Helper method to validate email address
    private boolean isValidEmail(String email) {
        // Basic email validation
        // You can use a regex or a library like Apache Commons Email for more robust validation
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private void sendNotificationEmail(String recipientEmail, Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        // Append the mail subject to the notification subject
        message.setSubject("New Mail Notification: " + mail.getSubject());
        message.setText("You have received a new mail with the subject: " + mail.getSubject() +
                        ". It was sent by " + mail.getSender() + ".");

        javaMailSender.send(message);
    }

    
    
    @Override
    public Mail archiveMail(Long mailId) {
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new MailNotFoundException("Mail not found"));

        mail.setStatus(MailStatus.ARCHIVED);
        mail.setArchivedDate(LocalDateTime.now());

        mailRepository.save(mail);
        sendNotificationEmail(mail.getRecipient(), "Mail Archived", "The mail with subject '" + mail.getSubject() + "' has been archived.");
        return mail;
    }

    private void sendNotificationEmail(String recipientEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }
    @Override
    public void deleteMail(Long mailId) {
        // Check if the mail exists
        Mail mail = mailRepository.findById(mailId).orElse(null);
        if (mail == null) {
            throw new RuntimeException("Mail with ID " + mailId + " not found");
        }
        
        // Perform the deletion
        mailRepository.delete(mail);
    }
   
    //////////////////////////////////////////////////////////

    // 4. Publication et diffusion (Coffre-fort numérique, complétude des données, extranet)
   /*@Override
    public void sendMail(Mail mail) {
        mail.setStatus(MailStatus.SENT);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getRecipient().getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());

        mailRepository.save(mail);


        mailSender.send(message);
    }*/
   /* 
    public void sendMail(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            if (to == null || to.isBlank()) {
                throw new IllegalArgumentException("To address cannot be null or empty");
            }

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException | IllegalArgumentException e) {
            e.printStackTrace();
            // Handle the exception or log it as needed
            throw new RuntimeException("Failed to send email");
        }
    }
    */
    
    
    
    
    
    
    
    
    
   
}