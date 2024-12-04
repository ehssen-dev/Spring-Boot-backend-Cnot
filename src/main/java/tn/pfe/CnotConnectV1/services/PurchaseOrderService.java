package tn.pfe.CnotConnectV1.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.InvoiceDTO;
import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.dto.ProjectDTO;
import tn.pfe.CnotConnectV1.dto.PurchaseOrderDTO;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.Supplier;
import tn.pfe.CnotConnectV1.enums.OrderStatus;
import tn.pfe.CnotConnectV1.enums.RequestStatus;
import tn.pfe.CnotConnectV1.repository.PurchaseRepository;
import tn.pfe.CnotConnectV1.repository.SupplierRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IInvoiceService;
import tn.pfe.CnotConnectV1.services.interfaces.IProcurementRequestService;
import tn.pfe.CnotConnectV1.services.interfaces.IProjectService;
import tn.pfe.CnotConnectV1.services.interfaces.IPurchaseOrderService;
import tn.pfe.CnotConnectV1.services.interfaces.ISupplierService;

@Service
public class PurchaseOrderService implements IPurchaseOrderService{
	
    private static final Logger logger = LogManager.getLogger(PurchaseOrderService.class);
    private static final Logger log = LogManager.getLogger(PurchaseOrderService.class);

    private final PurchaseRepository purchaseOrderRepository;
	private final SupplierRepository supplierRepository;
	
	@Autowired
    private IProcurementRequestService procurementRequestService;
	  @Autowired
	    private ISupplierService supplierService;

	    @Autowired
	    private IProjectService projectService;
	    @Autowired
	    private IInvoiceService invoiceService;
	    
    @Autowired
    public PurchaseOrderService(PurchaseRepository purchaseOrderRepository, ProcurementRequestService procurementRequestService, SupplierRepository supplierRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
		this.procurementRequestService = procurementRequestService;
		this.supplierRepository = supplierRepository;
    }
   
    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDTO dto) {
        try {
            
            logger.debug("Creating purchase order with data: {}", dto);

            Supplier supplier = supplierService.getSupplierById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            
            Project project = projectService.getProjectById(dto.getProjectId());
            
            
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            
            String generatedOrderNumber = generateOrderNumber();
            purchaseOrder.setOrderNumber(generatedOrderNumber);
            
            purchaseOrder.setQuantity(dto.getQuantity());
            purchaseOrder.setPurchaseName(dto.getPurchaseName());
            purchaseOrder.setDescription(dto.getDescription());
            purchaseOrder.setUnitPrice(dto.getUnitPrice());
            purchaseOrder.setPurchaseDate(dto.getPurchaseDate());
            purchaseOrder.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
            purchaseOrder.setStatus(dto.getStatus());
         //   purchaseOrder.setSupplier(supplier);
            purchaseOrder.setProject(project);

            logger.debug("Saving purchase order: {}", purchaseOrder);

          
            return purchaseOrderRepository.save(purchaseOrder);
        } catch (Exception e) {
            logger.error("Error occurred while creating purchase order: ", e);
            throw e;
        }
    }
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 7; 

    private String generateOrderNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);
        
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return sb.insert(LENGTH / 2, '-').toString();
    }

    @Override
    public PurchaseOrder updatePurchaseOrder(Long purchaseId, PurchaseOrderDTO dto) {

        if (!purchaseOrderRepository.existsById(purchaseId)) {
            throw new RuntimeException("PurchaseOrder not found");
        }

        Supplier supplier = supplierService.getSupplierById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Project project = projectService.getProjectById(dto.getProjectId());
        InvoiceDTO invoice = invoiceService.getInvoiceDTOById(dto.getInvoiceId());

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId).get();
        purchaseOrder.setQuantity(dto.getQuantity());
        purchaseOrder.setPurchaseName(dto.getPurchaseName());
        purchaseOrder.setDescription(dto.getDescription());
        purchaseOrder.setUnitPrice(dto.getUnitPrice());
        purchaseOrder.setPurchaseDate(dto.getPurchaseDate());
        purchaseOrder.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        purchaseOrder.setStatus(dto.getStatus());
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setProject(project);
        //purchaseOrder.setInvoice(dto.getInvoice());

        return purchaseOrderRepository.save(purchaseOrder);
    }


    @Override
    public List<PurchaseOrder> findAllByIds(List<Long> purchaseIds) {
        return purchaseOrderRepository.findAllById(purchaseIds);
    }
    
	@Override
    public List<PurchaseOrder> getPurchaseOrdersBySupplierAndAmount(Supplier supplier, double totalAmount) {
        return purchaseOrderRepository.findBySupplierAndTotalAmountGreaterThanEqual(supplier, totalAmount);
    }
	@Override
	public List<PurchaseOrder> getAllPurchaseOrders() {
	    try {
	        return purchaseOrderRepository.findAll();
	    } catch (Exception e) {
	        e.printStackTrace(); 
	        throw e; 
	    }
	}
	
	@Override
    public Optional<PurchaseOrder> getPurchaseOrderById(Long purchaseId) {
        return purchaseOrderRepository.findById(purchaseId);
    }
	@Override
	public Optional<Supplier> getSupplierById(Long supplierId) {
	    return supplierRepository.findById(supplierId);
	}
	
	@Override
    // Delete
    public boolean deletePurchaseOrder(Long purchaseId) {
        if (purchaseOrderRepository.existsById(purchaseId)) {
            purchaseOrderRepository.deleteById(purchaseId);
            return true;
        }
        return false; 
    }

	@Override
	// Generate Purchase Orders Automatically
	public void generatePurchaseOrders() {
	   
	    List<ProcurementRequestDTO> pendingRequestDTOs = procurementRequestService.getPendingRequestsDTO();

	    for (ProcurementRequestDTO requestDTO : pendingRequestDTOs) {
	        try {
	          
	            Supplier supplier = findSuitableSupplier();
	            if (supplier != null) {
	               
	                PurchaseOrder purchaseOrder = createPurchaseOrderFromRequestDTO(requestDTO, supplier);
	                if (purchaseOrder != null) {
	                   
	                    purchaseOrderRepository.save(purchaseOrder);
	                   
	                    procurementRequestService.updateRequestStatus(requestDTO.getRequestId(), RequestStatus.APPROVED);
	                } else {
	                    logger.warn("Failed to create purchase order for request ID: {}", requestDTO.getRequestId());
	                }
	            } else {
	                logger.warn("No suitable supplier found for request ID: {}", requestDTO.getRequestId());
	            }
	        } catch (Exception e) {
	            logger.error("Error processing procurement request ID: {}", requestDTO.getRequestId(), e);
	        }
	    }
	}

	@Override
    public Supplier findSuitableSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll();
        
        if (suppliers.isEmpty()) {
            return null;
        }

        Supplier selectedSupplier = suppliers.get(0);

        for (Supplier supplier : suppliers) {
           
            if (supplier.getOnTimeDeliveryRate() >= 0.8 &&
                supplier.getQualityControlRating() >= 4.0 &&
                supplier.getNumberOfPastIssues() == 0) {
            
                selectedSupplier = supplier;
                
                break;
            }
        }

        return selectedSupplier;
    }
	@Override
	@Transactional
	public PurchaseOrder createPurchaseOrderFromRequestDTO(ProcurementRequestDTO requestDTO, Supplier supplier) {
	    if (requestDTO == null || supplier == null) {
	        logger.warn("Procurement request DTO or supplier is null.");
	        return null;
	    }

	    PurchaseOrder purchaseOrder = new PurchaseOrder();
	
	    Integer quantity = requestDTO.getQuantity();
	    if (quantity == null) {
	        logger.warn("Quantity is null for procurement request ID: {}", requestDTO.getRequestId());
	        return null;
	    }
	    purchaseOrder.setQuantity(quantity);
	    purchaseOrder.setPurchaseName(requestDTO.getRequestedGoods());
	    purchaseOrder.setDescription(requestDTO.getDescription());
	    
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DAY_OF_MONTH, 7);
	    Date expectedDeliveryDate = cal.getTime();
	    purchaseOrder.setExpectedDeliveryDate(expectedDeliveryDate);
	   
	    purchaseOrder.setSupplier(supplier);
	    
	    Double unitPrice = getUnitPriceForRequest(requestDTO, supplier);
	    if (unitPrice == null || unitPrice <= 0) {
	        logger.warn("Invalid unit price for procurement request ID: {}", requestDTO.getRequestId());
	        return null;
	    }
	    purchaseOrder.setUnitPrice(unitPrice);
	    
	    return purchaseOrder;
	}
	public Double getUnitPriceForRequest(ProcurementRequestDTO requestDTO, Supplier supplier) {
        if (requestDTO == null || supplier == null) {
            logger.warn("ProcurementRequestDTO or Supplier is null");
            return null;
        }

        Double unitPrice = requestDTO.getEstimatedCost();

        if (unitPrice == null) {
            logger.warn("Unit price (estimated cost) is null for ProcurementRequestDTO ID: {}", 
                requestDTO.getRequestId());
            return null;
        }
        
        if (unitPrice <= 0) {
            logger.warn("Invalid unit price (<= 0) for ProcurementRequestDTO ID: {}, Unit Price: {}", 
                requestDTO.getRequestId(), unitPrice);
            return null;
        }

        return unitPrice;
    }

}
