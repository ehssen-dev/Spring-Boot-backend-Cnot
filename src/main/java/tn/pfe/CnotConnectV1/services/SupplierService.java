package tn.pfe.CnotConnectV1.services;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.SupplierDTO;
import tn.pfe.CnotConnectV1.entities.Supplier;
import tn.pfe.CnotConnectV1.repository.SupplierRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IPurchaseOrderService;
import tn.pfe.CnotConnectV1.services.interfaces.ISupplierService;

@Service
public class SupplierService  implements ISupplierService{

    @Autowired
    private SupplierRepository supplierRepository;


@Override
public Supplier createSupplier(SupplierDTO supplierDTO) {
    Supplier supplier = mapToEntity(supplierDTO);
    return supplierRepository.save(supplier);
}

private Supplier mapToEntity(SupplierDTO supplierDTO) {
    Supplier supplier = new Supplier();
    supplier.setName(supplierDTO.getName());
    supplier.setContactInformation(supplierDTO.getContactInformation());
    supplier.setAddress(supplierDTO.getAddress());
    supplier.setEmail(supplierDTO.getEmail());
    supplier.setOnTimeDeliveryRate(supplierDTO.getOnTimeDeliveryRate());
    supplier.setQualityControlRating(supplierDTO.getQualityControlRating());
    supplier.setNumberOfPastIssues(supplierDTO.getNumberOfPastIssues());
    return supplier;
}
@Override
    // Method to retrieve all suppliers
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
@Override
    // Method to retrieve a supplier by ID
    public Optional<Supplier> getSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId);
    }
@Override
public List<Supplier> getTopPerformingSuppliers(double minOnTimeDeliveryRate, double minQualityRating) {
    return supplierRepository.findAll().stream()
            .filter(supplier -> supplier.getOnTimeDeliveryRate() >= minOnTimeDeliveryRate &&
                                supplier.getQualityControlRating() >= minQualityRating)
            .collect(Collectors.toList());
}
@Override
public Supplier findBestSupplier() {
    List<Supplier> suppliers = supplierRepository.findAll();
    if (suppliers.isEmpty()) {
        throw new NoSuchElementException("No suppliers found.");
    }

    // Find the supplier that meets the criteria and ranks the best based on your conditions
    return suppliers.stream()
            .filter(supplier -> supplier.getOnTimeDeliveryRate() >= 0.8 &&
                    supplier.getQualityControlRating() >= 4.0 &&
                    supplier.getNumberOfPastIssues() == 0)
            .max(Comparator.comparingDouble(supplier -> 
                    supplier.getOnTimeDeliveryRate() * 0.5 + 
                    supplier.getQualityControlRating() * 0.5)) 
            .orElseThrow(() -> new NoSuchElementException("No suitable supplier found."));
}

@Override
public List<Supplier> findBestSuppliers() {
    List<Supplier> suppliers = supplierRepository.findAll();
    if (suppliers.isEmpty()) {
        throw new NoSuchElementException("No suppliers found.");
    }
    return suppliers.stream()
            .filter(supplier -> supplier.getOnTimeDeliveryRate() >= 0.8 &&
                    supplier.getQualityControlRating() >= 4.0 &&
                    supplier.getNumberOfPastIssues() == 0)
            .collect(Collectors.toList());
}
@Override
public Supplier updateSupplier(Long supplierId, SupplierDTO updatedSupplierDTO) {
    Supplier existingSupplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + supplierId));

    Supplier updatedSupplier = mapToEntity(updatedSupplierDTO);
    
    existingSupplier.setName(updatedSupplier.getName());
    existingSupplier.setContactInformation(updatedSupplier.getContactInformation());
    existingSupplier.setAddress(updatedSupplier.getAddress());
    existingSupplier.setEmail(updatedSupplier.getEmail());
    existingSupplier.setOnTimeDeliveryRate(updatedSupplier.getOnTimeDeliveryRate());
    existingSupplier.setQualityControlRating(updatedSupplier.getQualityControlRating());
    existingSupplier.setNumberOfPastIssues(updatedSupplier.getNumberOfPastIssues());

    return supplierRepository.save(existingSupplier);
}

@Override
    // Method to delete a supplier
    public void deleteSupplier(Long supplierId) {
        supplierRepository.deleteById(supplierId);
    }
}