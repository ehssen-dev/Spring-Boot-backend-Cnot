package tn.pfe.CnotConnectV1.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tn.pfe.CnotConnectV1.ProcessingLog.UsageReport;
import tn.pfe.CnotConnectV1.entities.Mail;
import tn.pfe.CnotConnectV1.enums.MailType;
import tn.pfe.CnotConnectV1.repository.MailRepository;
import tn.pfe.CnotConnectV1.repository.ProcessingLogRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IMailAnalyticsService;

@Service
public class MailAnalyticsService implements IMailAnalyticsService{

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private ProcessingLogRepository processingLogRepository;
    
    @Override
    public UsageReport generateUsageReport(LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        long totalMails = mailRepository.countMailsBetweenDates(startDate, endDate);
        double averageProcessingTime = calculateAverageProcessingTime(startDateTime, endDateTime);
        
        // Adjusting mailTypeDistribution to correctly handle enum MailType
        Map<MailType, Long> mailTypeDistribution = mailRepository.getMailTypeDistribution(startDate, endDate);
        
        // Assuming UsageReport has a constructor that accepts long, double, and a Map<MailType, Long>
        return new UsageReport(totalMails, averageProcessingTime, mailTypeDistribution);
    }

    public double calculateAverageProcessingTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Mail> mails = mailRepository.findByReceivedDateBetween(startDateTime, endDateTime);
        long totalMinutes = 0;
        int count = 0;
        
        for (Mail mail : mails) {
            if (mail.getReceivedDate() != null && mail.getProcessedDate() != null) {
                long minutes = ChronoUnit.MINUTES.between(mail.getReceivedDate(), mail.getProcessedDate());
                totalMinutes += minutes;
                count++;
            }
        }
        
        return count > 0 ? (double) totalMinutes / count : 0;
    }


    /*
    @Override
    public AnalyticsData generateAnalyticsData(LocalDate startDate, LocalDate endDate) {
        List<Bottleneck> bottlenecks = processingLogRepository.findBottlenecks(startDate, endDate);
        
        // Assuming mailRepository has the necessary methods to retrieve trends and peak times
        List<ProcessingTimeTrend> processingTimeTrends = processingLogRepository.getProcessingTimeTrends(startDate, endDate);
        List<PeakTime> peakTimes = processingLogRepository.getPeakTimes(startDate, endDate);

        return new AnalyticsData(bottlenecks, processingTimeTrends, peakTimes);
    }*/
}