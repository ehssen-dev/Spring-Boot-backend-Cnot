 package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Mail;
import tn.pfe.CnotConnectV1.enums.MailStatus;
import tn.pfe.CnotConnectV1.enums.MailType;
import tn.pfe.CnotConnectV1.enums.Qualification;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> , JpaSpecificationExecutor<Mail>{
   /* @Query("SELECT m FROM Mail m WHERE " +
            "(:receivedDate IS NULL OR m.receivedDate >= :receivedDate) " +
            "AND (:senderEmail IS NULL OR m.senderEmail = :senderEmail) " +
            "AND (:recipientEmails IS NULL OR m.recipientEmails = :recipientEmails) " +
            "AND (:department  IS NULL OR m.department = :department)")
    List<Mail> searchMail(@Param("receivedDate") Date dateReceived,
                          @Param("senderEmail") User sender,
                          @Param("recipientEmails") User recipient,
                          @Param("department") Department department);

    List<Mail> findByDepartments(Department department);
    List<Mail> findByReceiver(User receiver);
    List<Mail> findBySender(User sender);*/
	
	   List<Mail> findByAthleteAthleteId(Long athleteId);
	
	
	    List<Mail> findByQualificationType(Qualification qualificationType);
	    List<Mail> findByStatus(MailStatus status);
	    List<Mail> findByType(MailType type);
	    
	    List<Mail> findByAthlete_athleteId(Long athleteId);
	    List<Mail> findByDelegation_delegationId(Long delegationId);
	    List<Mail> findByUser_userId(Long userId);

	    List<Mail> findBySenderContaining(String sender);

	    List<Mail> findByRecipientContaining(String recipient);

	    List<Mail> findBySubjectContaining(String subject);

	    List<Mail> findBySentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	    
	    @Query("SELECT m FROM Mail m WHERE m.sender LIKE %:sender% AND m.status = :status")
	    List<Mail> findBySenderAndStatus(@Param("sender") String sender, @Param("status") MailStatus status);

	    Page<Mail> findAll(Specification<Mail> spec, Pageable pageable);

	  /*  @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, m.receivedDate, m.processedDate)) FROM Mail m WHERE m.receivedDate >= :startDate AND m.receivedDate <= :endDate")
	    double calculateAverageProcessingTime(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);*/
	    List<Mail> findByReceivedDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

	    @Query("SELECT COUNT(m) FROM Mail m WHERE m.receivedDate BETWEEN :startDate AND :endDate")
	    long countMailsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	    @Query("SELECT COUNT(m) FROM Mail m WHERE m.type = :mailType")
	    long countMailsByType(@Param("mailType") MailType mailType);
	    
	    @Query("SELECT m FROM Mail m WHERE m.receivedDate BETWEEN :startDate AND :endDate")
	    List<Mail> findBottlenecks(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	    @Query("SELECT m.type AS mailType, COUNT(m) AS count FROM Mail m WHERE m.receivedDate BETWEEN :startDate AND :endDate GROUP BY m.type")
	    Map<MailType, Long> getMailTypeDistribution(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}