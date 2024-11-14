package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.SupplierDTO;
import tn.pfe.CnotConnectV1.entities.Supplier;
import tn.pfe.CnotConnectV1.services.interfaces.ISupplierService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:3000") 
public class SupplierController {

    private final ISupplierService supplierService;

    @Autowired
    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/add")
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        Supplier createdSupplier = supplierService.createSupplier(supplierDTO);
        SupplierDTO responseDTO = mapToDTO(createdSupplier);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    private SupplierDTO mapToDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getName(),
                supplier.getContactInformation(),
                supplier.getAddress(),
                supplier.getEmail(),
                supplier.getOnTimeDeliveryRate(),
                supplier.getQualityControlRating(),
                supplier.getNumberOfPastIssues()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    @GetMapping("/top-performing")
    public List<Supplier> getTopPerformingSuppliers(
            @RequestParam double minOnTimeDeliveryRate,
            @RequestParam double minQualityRating) {
        return supplierService.getTopPerformingSuppliers(minOnTimeDeliveryRate, minQualityRating);
    }
    @GetMapping("/best")
    public ResponseEntity<Supplier> findBestSupplier() {
        Supplier bestSupplier = supplierService.findBestSupplier();
        return ResponseEntity.ok(bestSupplier);
    }
    @GetMapping("/best-suppliers")
    public ResponseEntity<List<Supplier>> getBestSuppliers() {
        List<Supplier> bestSuppliers = supplierService.findBestSuppliers();
        if (bestSuppliers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bestSuppliers);
        }
        return ResponseEntity.ok(bestSuppliers);
    }
    @GetMapping("/{supplierId}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long supplierId) {
        Optional<Supplier> supplier = supplierService.getSupplierById(supplierId);
        return supplier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable("supplierId") Long supplierId, 
            @RequestBody SupplierDTO supplierDTO) {
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, supplierDTO);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }
}