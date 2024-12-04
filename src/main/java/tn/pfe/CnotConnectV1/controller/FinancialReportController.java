package tn.pfe.CnotConnectV1.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.FinancialDTO;
import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.exeptions.ResourceNotFoundException;
import tn.pfe.CnotConnectV1.services.interfaces.IFinancialReportService;

@RestController
@RequestMapping("/api/financial-reports")
@CrossOrigin(origins = "http://localhost:3000") 
public class FinancialReportController {
	
	@Autowired
    private final IFinancialReportService financialReportService;

    @Autowired
    public FinancialReportController(IFinancialReportService financialReportService) {
        this.financialReportService = financialReportService;
    }
    private static final Logger logger = LoggerFactory.getLogger(FinancialReportController.class);
   
    
    @PostMapping("/generate")
    public ResponseEntity<FinancialDTO> generateFinancialReport(
        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
        @RequestParam("projectId") Long projectId) {

        try {
            FinancialDTO financialDTO = financialReportService.generateFinancialReportForPeriod(startDate, endDate, projectId);

            if (financialDTO != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(financialDTO);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid arguments for generating financial report: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Unexpected error while generating financial report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/chart-data")
    public ResponseEntity<Map<String, Double>> getFinancialReportChartData(
        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
        @RequestParam("projectId") Long projectId) {

        try {
            Map<String, Double> chartData = financialReportService.getFinancialReportChartData(projectId, startDate, endDate);

            if (chartData.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching chart data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    } 
    
    @GetMapping("/all")
    public List<FinancialReport> getAllFinancialReports() {
        return financialReportService.findAll();
    }

    @GetMapping("/{tReportId}")
    public ResponseEntity<FinancialReport> getFinancialReportById(@PathVariable Long tReportId) {
        Optional<FinancialReport> technicalReport = financialReportService.findById(tReportId);
        return technicalReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getFinancialReportsByProjectId(@PathVariable Long projectId) {
        try {
            List<FinancialReport> reports = financialReportService.getFinancialReportsByProjectId(projectId);
            if (reports.isEmpty()) {
                return ResponseEntity.noContent().build(); 
            }
            return ResponseEntity.ok(reports);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



}