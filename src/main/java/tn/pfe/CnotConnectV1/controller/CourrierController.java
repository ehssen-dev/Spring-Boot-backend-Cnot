package tn.pfe.CnotConnectV1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.entities.Courrier;
import tn.pfe.CnotConnectV1.services.MailService;

@RestController
@RequestMapping("/api/courriers")
@CrossOrigin(origins = "http://localhost:3000") 
public class CourrierController {

    @Autowired
    private MailService courrierService;

   /* @GetMapping
    public List<Courrier> getAllCourriers() {
        return courrierService.getAllCourriers();
    }

    @PostMapping
    public Courrier createCourrier(@RequestBody Courrier courrier) {
        return courrierService.saveCourrier(courrier);
    }*/

    // Endpoint to trigger email fetching
    @GetMapping("/fetch-emails")
    public String fetchEmails() {
        try {
            courrierService.fetchEmails();
            return "Emails fetched successfully";
        } catch (Exception e) {
            return "Error fetching emails: " + e.getMessage();
        }
    }
}