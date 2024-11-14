package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.SupplierDTO;
import tn.pfe.CnotConnectV1.entities.Supplier;

public interface ISupplierService {

	void deleteSupplier(Long supplierId);

	//Supplier updateSupplier(Long supplierId, Supplier updatedSupplier);

	Optional<Supplier> getSupplierById(Long supplierId);

	List<Supplier> getAllSuppliers();

	
    List<Supplier> getTopPerformingSuppliers(double minOnTimeDeliveryRate, double minQualityRating);
    
    Supplier findBestSupplier();

	Supplier createSupplier(SupplierDTO supplierDTO);

	Supplier updateSupplier(Long supplierId, SupplierDTO updatedSupplierDTO);

	List<Supplier> findBestSuppliers();

}
