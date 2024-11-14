package tn.pfe.CnotConnectV1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.InvoiceDTO;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.exeptions.NotFoundException;
import tn.pfe.CnotConnectV1.services.interfaces.IInvoiceService;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:3000") 
public class InvoiceController {

    private final IInvoiceService invoiceService;

    @Autowired
    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/add")
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        Invoice savedInvoice = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvoice);
    }

    @PutMapping("/update/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable("invoiceId") Long invoiceId,
            @RequestBody InvoiceDTO invoiceDTO) {
        Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, invoiceDTO);
        return ResponseEntity.ok().body(updatedInvoice);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok().body(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable("id") Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceDTOById(invoiceId);

        if (invoiceDTO != null) {
            return new ResponseEntity<>(invoiceDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
   

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
        boolean deleted = invoiceService.deleteInvoice(invoiceId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/process")
    public ResponseEntity<String> processInvoices() {
        invoiceService.processInvoices();
        return ResponseEntity.ok().body("Invoices processed successfully");
    }

   
    @PostMapping("/{invoiceId}/mark-as-paid")
    public ResponseEntity<String> markInvoiceAsPaid(@PathVariable("invoiceId") Long invoiceId) {
        try {
            Invoice paidInvoice = invoiceService.markInvoiceAsPaid(invoiceId);
            return ResponseEntity.ok("Invoice with ID " + invoiceId + " has been successfully marked as paid.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    
}