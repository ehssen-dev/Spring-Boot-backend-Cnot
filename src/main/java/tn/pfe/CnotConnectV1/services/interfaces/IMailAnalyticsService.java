package tn.pfe.CnotConnectV1.services.interfaces;

import java.time.LocalDate;

import tn.pfe.CnotConnectV1.ProcessingLog.UsageReport;

public interface IMailAnalyticsService {

	UsageReport generateUsageReport(LocalDate startDate, LocalDate endDate);

	//AnalyticsData generateAnalyticsData(LocalDate startDate, LocalDate endDate);

}
