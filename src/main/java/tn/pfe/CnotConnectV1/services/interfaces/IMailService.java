package tn.pfe.CnotConnectV1.services.interfaces;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import tn.pfe.CnotConnectV1.dto.EntityEmailsDTO;
import tn.pfe.CnotConnectV1.dto.MailRequest;
import tn.pfe.CnotConnectV1.entities.Mail;
import tn.pfe.CnotConnectV1.enums.MailStatus;
import tn.pfe.CnotConnectV1.enums.Qualification;

public interface IMailService {
    

	EntityEmailsDTO getAllEntityEmails();
	List<String> getAllEmails();

	//void sendMailToAllEntities(String senderEmail, MailRequest mailRequest, List<MultipartFile> files);

	
	Page<Mail> searchMails(String sender, String recipient, String subject, LocalDateTime sentDate,
			MailStatus status, Pageable pageable);
	
	
	////////////////////
	
    // 1. Acquisition du courrier
    void receiveMail(Mail mail);
    
    // 2. Processus courrier (Qualification et distribution)    
    void qualifyAndDistributeMail(Long mailId, Qualification qualificationType);
    
    // 3. Stockage et consultation (Classement structuré, indexation, consultation)
    List<Mail> getAllMails();
    Mail getMailById(Long mailId);
    
    // 4. Publication et diffusion (Coffre-fort numérique, complétude des données, extranet)
    void sendMail(Mail mail);
    

	Mail archiveMail(Long mailId);
	
	/*void sendMails(String senderEmail, MailRequest mailRequest, List<MultipartFile> files)
			throws IOException, MessagingException;*/
	/*void sendMailToAllAthletes(String senderEmail, MailRequest mailRequest, List<MultipartFile> files)
			throws IOException;*/
	void sendMailToAllDelegations(String senderEmail, MailRequest mailRequest, List<MultipartFile> files)
			throws IOException;
	void sendMailToAllUsers(String senderEmail, MailRequest mailRequest, List<MultipartFile> files) throws IOException;
	
	List<Mail> getMailsByAthleteId(Long athleteId);
	void sendMailsV(MailRequest mailRequest, List<MultipartFile> files) throws IOException, MessagingException;
	void sendMailToAllEntities(MailRequest mailRequest, List<MultipartFile> files);
	void sendMailToAllAthletes(MailRequest mailRequest, List<MultipartFile> files) throws IOException;
	void deleteMail(Long mailId);
	
	
	
	
	//void sendMails(MailRequest mailRequest);
	//void sendMailToAllEntities();
	//void sendMailToAllAthletes();
	//void sendMailToAllDelegations();
	//void sendMailToAllUsers();
//	void sendMailToAllAthletes(String senderEmail, String subject, String content);
	//void sendMailToAllDelegations(String senderEmail, String subject, String content);
	//void sendMailToAllUsers(String senderEmail, String subject, String content);
	//void sendMailToAllEntities(String senderEmail, String subject, String content);
	//void sendMails(String senderEmail, MailRequest mailRequest);
	//void sendMails(String senderEmail, MailRequest mailRequest, List<MultipartFile> files);

	//void sendMails(MailRequest mailRequest, Qualification qualificationType);

}


	