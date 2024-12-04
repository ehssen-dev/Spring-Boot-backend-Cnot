package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.Scheduled.MonitoringService;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

  
    @RequestMapping("/evaluate")
    public String evaluateGamesAndEventsManually() {
        StringBuilder responseMessage = new StringBuilder();
        try {
            // Trigger the evaluation manually
        	monitoringService.evaluateGamesAndEvents();
            responseMessage.append("Games , Results and events evaluation completed successfully!\n");
        } catch (Exception e) {
            responseMessage.append("Error during evaluation: ").append(e.getMessage()).append("\n");
        }
        
        return responseMessage.toString();
    }
}