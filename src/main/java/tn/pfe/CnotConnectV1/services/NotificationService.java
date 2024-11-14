package tn.pfe.CnotConnectV1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.services.interfaces.INotificationService;

@Service
public class NotificationService implements INotificationService{
	
	@Value("${spring.mail.username}")
    private String from;
	@Autowired
    private JavaMailSender mailSender;
	
@Override
    public void notifyUsers(User recipient, String subject, String message) {
        
        System.out.println("Notification sent to " + recipient.getEmail() + ": Subject - " + subject + ", Message - " + message);
    }

@Override
public void sendNotification(User user, String message) {
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setFrom(from);
    mail.setTo(user.getEmail());
    mail.setSubject("Proposal Request");
    mail.setText(message);
    mailSender.send(mail);
}
}