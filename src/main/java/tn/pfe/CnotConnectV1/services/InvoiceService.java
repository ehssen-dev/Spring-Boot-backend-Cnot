package tn.pfe.CnotConnectV1.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.InvoiceDTO;
import tn.pfe.CnotConnectV1.dto.PurchaseOrderDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.Supplier;
import tn.pfe.CnotConnectV1.enums.InvoiceStatus;
import tn.pfe.CnotConnectV1.exeptions.NotFoundException;
import tn.pfe.CnotConnectV1.repository.BudgetAllocationRepository;
import tn.pfe.CnotConnectV1.repository.FinancialReportRepository;
import tn.pfe.CnotConnectV1.repository.InvoiceRepository;
import tn.pfe.CnotConnectV1.repository.PurchaseRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IInvoiceService;
import tn.pfe.CnotConnectV1.services.interfaces.IProjectService;
import tn.pfe.CnotConnectV1.services.interfaces.IPurchaseOrderService;

@Service
public class InvoiceService implements IInvoiceService{
	private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;
    private final IPurchaseOrderService purchaseOrderService;
    private final IProjectService projectService;
  
    @Autowired
    private BudgetAllocationRepository budgetAllocationRepository;

    @Autowired
    private FinancialReportRepository financialReportRepository;

    @Autowired
    private PurchaseRepository purchaseOrderRepository;
    
    @Autowired
    public InvoiceService(@Lazy InvoiceRepository invoiceRepository, IProjectService projectService, @Lazy IPurchaseOrderService purchaseOrderService) {
        this.invoiceRepository = invoiceRepository;
		this.purchaseOrderService = purchaseOrderService;
		this.projectService = projectService;
    }
   @Override
    // Create
    public Invoice saveInvoice(Invoice invoice) {
        if (invoice.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than 0");
        }
        return invoiceRepository.save(invoice);
    }
   @Override
   public Invoice createInvoice(InvoiceDTO invoiceDTO) {
       if (invoiceDTO.getProjectId() == null) {
           throw new IllegalArgumentException("Project ID must not be null");
       }

       // Fetch the Project entity by ID
       Project project = projectService.getProjectById(invoiceDTO.getProjectId());

       // Create and populate the Invoice entity
       Invoice invoice = new Invoice();
       invoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
       invoice.setDueDate(invoiceDTO.getDueDate());
       invoice.setPaid(invoiceDTO.isPaid());
       invoice.setDescription(invoiceDTO.getDescription());
       invoice.setStatus(InvoiceStatus.valueOf(invoiceDTO.getStatus()));
       invoice.setProject(project);

       // Fetch all Purchase Orders related to the Project
       List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByProject_ProjectId(invoiceDTO.getProjectId());

       // Calculate the total amount from all Purchase Orders
       double totalAmount = purchaseOrders.stream()
           .mapToDouble(PurchaseOrder::getTotalAmount) // Assuming PurchaseOrder has a method getAmount()
           .sum();

       // Set the total amount in the Invoice
       invoice.setTotalAmount(totalAmount);

       // Set the Purchase Orders in the Invoice
       invoice.setPurchaseOrders(purchaseOrders);
       for (PurchaseOrder purchaseOrder : purchaseOrders) {
           purchaseOrder.setInvoice(invoice); // Link each PurchaseOrder to this Invoice
       }

       // Generate the Invoice Number
       String generatedInvoiceNumber = generateInvoiceNumber(); // Assuming you have this method
       invoice.setInvoiceNumber(generatedInvoiceNumber);

       // Save the Invoice
       return invoiceRepository.save(invoice);
   }

	 private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    private static final int LENGTH = 6; 

	    private String generateInvoiceNumber() {
	        SecureRandom random = new SecureRandom();
	        StringBuilder sb = new StringBuilder(LENGTH);
	        
	        for (int i = 0; i < LENGTH; i++) {
	            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
	            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
	        }
	        return sb.insert(LENGTH / 2, '-').toString();
	    }

@Override
public Invoice updateInvoice(Long invoiceId, InvoiceDTO invoiceDTO) {
    if (invoiceId == null || invoiceDTO == null) {
        throw new IllegalArgumentException("Invoice ID and DTO must not be null");
    }

    Invoice existingInvoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceId));

    // Update fields
    existingInvoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
    existingInvoice.setTotalAmount(invoiceDTO.getTotalAmount());
    existingInvoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
    existingInvoice.setDueDate(invoiceDTO.getDueDate());
    existingInvoice.setPaid(invoiceDTO.isPaid());
    existingInvoice.setDescription(invoiceDTO.getDescription());
    existingInvoice.setStatus(InvoiceStatus.valueOf(invoiceDTO.getStatus()));

    // Update related entities
    if (invoiceDTO.getProjectId() != null) {
        Project project = projectService.getProjectById(invoiceDTO.getProjectId());
        existingInvoice.setProject(project);
    } else {
        existingInvoice.setProject(null);
    }

    if (invoiceDTO.getBudgetId() != null) {
        BudgetAllocation budget = budgetAllocationRepository.findById(invoiceDTO.getBudgetId()).orElse(null);
        existingInvoice.setBudget(budget);
    } else {
        existingInvoice.setBudget(null);
    }

    if (invoiceDTO.getFinancialReportId() != null) {
        FinancialReport financialReport = financialReportRepository.findById(invoiceDTO.getFinancialReportId()).orElse(null);
        existingInvoice.setFinancialReport(financialReport);
    } else {
        existingInvoice.setFinancialReport(null);
    }

    // Update purchase orders
    if (invoiceDTO.getPurchaseOrderIds() != null) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllById(invoiceDTO.getPurchaseOrderIds());
        for (PurchaseOrder purchaseOrder : purchaseOrders) {
            purchaseOrder.setInvoice(existingInvoice); // Link each PurchaseOrder to this Invoice
        }
        existingInvoice.setPurchaseOrders(purchaseOrders);
    } else {
        existingInvoice.setPurchaseOrders(Collections.emptyList());
    }

    return invoiceRepository.save(existingInvoice);
}

@Override
    // Read
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }


/*@Override
    public Optional<Invoice> getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }*/
@Override
public InvoiceDTO getInvoiceDTOById(Long invoiceId) {
    Optional<Invoice> invoiceOpt = invoiceRepository.findByIdWithDetails(invoiceId);

    if (invoiceOpt.isPresent()) {
        Invoice invoice = invoiceOpt.get();

        // Convert Invoice entity to InvoiceDTO
        List<PurchaseOrderDTO> purchaseOrderDTOs = invoice.getPurchaseOrders().stream()
        		.map(po -> new PurchaseOrderDTO(
        			    po.getPurchaseId(),
        			    po.getOrderNumber(),
        			    po.getQuantity(),
        			    po.getPurchaseName(),
        			    po.getUnitPrice(), 
        			    po.getTotalAmount(), 
        			    po.getPurchaseDate(), 
        			    po.getSupplier() != null ? po.getSupplier().getSupplierId() : null, 
        			    po.getProject() != null ? po.getProject().getProjectId() : null, 
        			    po.getInvoice() != null ? po.getInvoice().getInvoiceId() : null  
        			))
                .collect(Collectors.toList());

        return new InvoiceDTO(
            invoice.getInvoiceId(),
            invoice.getInvoiceNumber(),
            invoice.getTotalAmount(),
            invoice.getInvoiceDate(),
            invoice.getDueDate(),
            invoice.isPaid(),
            invoice.getDescription(),
            invoice.getStatus().name(), // Assuming InvoiceStatus is an enum and converting to String
            invoice.getBudget() != null ? invoice.getBudget().getBudgetId() : null,
            invoice.getFinancialReport() != null ? invoice.getFinancialReport().getFReportId() : null,
            invoice.getProject() != null ? invoice.getProject().getProjectId() : null,
            purchaseOrderDTOs
        );
    }

    return null; 
}

@Override
    // Delete
    public boolean deleteInvoice(Long invoiceId) {
    	 if (!invoiceRepository.existsById(invoiceId)) {
             throw new EntityNotFoundException("Invoice not found with id: " + invoiceId);
         }
        if (invoiceRepository.existsById(invoiceId)) {
            invoiceRepository.deleteById(invoiceId);
            return true;
        }
        return false; 
    }
	@Override
	// Automate Invoice Processing
    public void processInvoices() {
		logger.info("Starting invoice processing");
        List<Invoice> pendingInvoices = invoiceRepository.findByStatus(InvoiceStatus.PENDING);

        // Process each pending invoice
        for (Invoice invoice : pendingInvoices) {
            // Validate the invoice
            if (validateInvoice(invoice)) {
                // Match the invoice with purchase orders
                List<PurchaseOrder> matchingPurchaseOrders = matchWithPurchaseOrders(invoice);

                // If matching purchase orders found, mark the invoice as approved
                if (!matchingPurchaseOrders.isEmpty()) {
                    invoice.setStatus(InvoiceStatus.APPROVED);
                    invoiceRepository.save(invoice);

                    // Additional logic such as updating purchase orders, generating financial reports, etc.
                }
            }
        }
        logger.info("Finished processing invoices");
    }
	
	private boolean validateInvoice(Invoice invoice) {
	    return invoice.getTotalAmount() > 0 && invoice.getInvoiceDate() != null;
	}


    // Match Invoice with Purchase Orders 
    private List<PurchaseOrder> matchWithPurchaseOrders(Invoice invoice) {
        List<PurchaseOrder> purchaseOrders = invoice.getPurchaseOrders();
        if (purchaseOrders != null && !purchaseOrders.isEmpty()) {
            // Collect unique suppliers associated with the invoice's purchase orders
            Set<Supplier> suppliers = purchaseOrders.stream()
                .map(PurchaseOrder::getSupplier)
                .collect(Collectors.toSet());

            List<PurchaseOrder> matchedPurchaseOrders = new ArrayList<>();
            // Match each supplier's purchase orders
            for (Supplier supplier : suppliers) {
                // Example matching logic
                List<PurchaseOrder> matchedOrders = purchaseOrderService.getPurchaseOrdersBySupplierAndAmount(supplier, invoice.getTotalAmount());
                matchedPurchaseOrders.addAll(matchedOrders);
            }
            return matchedPurchaseOrders;
        }
        return Collections.emptyList();
    }
    
    
    
    
    
  
    @Override
    public Invoice markInvoiceAsPaid(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found with id: " + invoiceId));

        if (invoice.isPaid()) {
            throw new IllegalStateException("Invoice with id " + invoiceId + " is already marked as paid.");
        }
        invoice.setPaid(true);

        return invoiceRepository.save(invoice);
    }


}