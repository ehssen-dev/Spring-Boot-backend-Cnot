package tn.pfe.CnotConnectV1.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.PurchaseOrderDTO;
import tn.pfe.CnotConnectV1.dto.PurchaseOrderRequest;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.Supplier;
import tn.pfe.CnotConnectV1.services.interfaces.IPurchaseOrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:3000") 
public class PurchaseOrderController {

    private final IPurchaseOrderService purchaseOrderService;
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Autowired
    public PurchaseOrderController(IPurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDTO);
            return new ResponseEntity<>(createdPurchaseOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error occurred: ", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); 
        } catch (Exception e) {
            logger.error("Unexpected error occurred: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }


    @PutMapping("/update/{purchaseId}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
            @PathVariable Long purchaseId, @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updatePurchaseOrder(purchaseId, purchaseOrderDTO);
            return new ResponseEntity<>(updatedPurchaseOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }



    @GetMapping("/supplier/{supplierId}/amount/{totalAmount}")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrdersBySupplierAndAmount(@PathVariable Long supplierId,
                                                                                    @PathVariable double totalAmount) {
        try {
            Optional<Supplier> supplier = purchaseOrderService.getSupplierById(supplierId);
            if (supplier.isPresent()) {
                List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchaseOrdersBySupplierAndAmount(supplier.get(), totalAmount);
                return new ResponseEntity<>(purchaseOrders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        try {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
            return new ResponseEntity<>(purchaseOrders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long purchaseId) {
        try {
            Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderById(purchaseId);
            return purchaseOrder.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<HttpStatus> deletePurchaseOrder(@PathVariable Long purchaseId) {
        try {
            purchaseOrderService.deletePurchaseOrder(purchaseId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generatePurchaseOrders() {
        try {
            purchaseOrderService.generatePurchaseOrders();
            return new ResponseEntity<>("Purchase orders generated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to generate purchase orders", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}