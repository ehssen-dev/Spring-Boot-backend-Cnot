package tn.pfe.CnotConnectV1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.ProcessingLog;

import java.util.List;

@Repository
public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, Long> {

	List<ProcessingLog> findByMailId(String mailId);
    List<ProcessingLog> findByStage(String stage);
    List<ProcessingLog> findByStatus(String status);
    
   /* @Query("SELECT stage, AVG(TIMESTAMPDIFF(MINUTE, timestamp, LEAD(timestamp) OVER (PARTITION BY mailId ORDER BY timestamp))) AS avgDuration " +
            "FROM ProcessingLog WHERE timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY stage ORDER BY avgDuration DESC")
     List<Bottleneck> findBottlenecks(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.example.analytics.dto.ProcessingTimeTrend(DATE(p.timestamp), AVG(TIMESTAMPDIFF(MINUTE, p.timestamp, LEAD(p.timestamp) OVER (PARTITION BY p.mailId ORDER BY p.timestamp)))) " +
            "FROM ProcessingLog p " +
            "WHERE p.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(p.timestamp) " +
            "ORDER BY DATE(p.timestamp)")
     List<ProcessingTimeTrend> getProcessingTimeTrends(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT HOUR(p.timestamp) AS hour, COUNT(p) AS mailCount " +
            "FROM ProcessingLog p " +
            "WHERE p.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY HOUR(p.timestamp) " +
            "ORDER BY mailCount DESC")
     List<PeakTime> getPeakTimes(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
*/
 }
