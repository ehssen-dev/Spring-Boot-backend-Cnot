package tn.pfe.CnotConnectV1.services;

import java.time.LocalDateTime;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import tn.pfe.CnotConnectV1.entities.Mail;
import tn.pfe.CnotConnectV1.enums.MailStatus;

public class MailSpecification {

	public static Specification<Mail> byCriteria(String sender, String recipient, String subject, LocalDateTime sentDate, MailStatus status) {
	    return (root, query, criteriaBuilder) -> {
	        Predicate predicate = criteriaBuilder.conjunction();

	        if (sender != null) {
	            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("sender"), "%" + sender + "%"));
	        }
	        if (recipient != null) {
	            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("recipient"), "%" + recipient + "%"));
	        }
	        if (subject != null) {
	            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("subject"), "%" + subject + "%"));
	        }
	        if (sentDate != null) {
	            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("sentDate"), sentDate));
	        }
	        if (status != null) {
	            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
	        }

	        return predicate;
	    };
	}

}