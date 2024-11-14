package tn.pfe.CnotConnectV1.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.ProcessingLog.UsageReport;
import tn.pfe.CnotConnectV1.services.interfaces.IMailAnalyticsService;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000") 
public class AnalyticsController {

    @Autowired
    private IMailAnalyticsService mailAnalyticsService;

    @GetMapping("/usage")
    public ResponseEntity<UsageReport> getUsageReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        UsageReport report = mailAnalyticsService.generateUsageReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }
/*
    @GetMapping("/data")
    public ResponseEntity<AnalyticsData> getAnalyticsData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        AnalyticsData data = mailAnalyticsService.generateAnalyticsData(startDate, endDate);
        return ResponseEntity.ok(data);
    }*/
}
