package tn.pfe.CnotConnectV1.ProcessingLog;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import tn.pfe.CnotConnectV1.enums.MailType;

@Getter
@Setter
public class UsageReport {
	  private long totalMails;
	    private double averageProcessingTime;
	    private Map<MailType, Long> mailTypeDistribution;

	    public UsageReport(long totalMails, double averageProcessingTime, Map<MailType, Long> mailTypeDistribution) {
	        this.totalMails = totalMails;
	        this.averageProcessingTime = averageProcessingTime;
	        this.mailTypeDistribution = mailTypeDistribution;
	    }
}