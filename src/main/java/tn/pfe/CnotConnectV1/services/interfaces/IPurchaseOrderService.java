package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.dto.PurchaseOrderDTO;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.Supplier;

public interface IPurchaseOrderService {


	Supplier findSuitableSupplier();

	void generatePurchaseOrders();

	boolean deletePurchaseOrder(Long purchaseId);


	Optional<PurchaseOrder> getPurchaseOrderById(Long purchaseId);

	List<PurchaseOrder> getAllPurchaseOrders();

	List<PurchaseOrder> getPurchaseOrdersBySupplierAndAmount(Supplier supplier, double totalAmount);

	//PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);

	Optional<Supplier> getSupplierById(Long supplierId);

	List<PurchaseOrder> findAllByIds(List<Long> purchaseIds);



	PurchaseOrder createPurchaseOrder(PurchaseOrderDTO dto);

	PurchaseOrder updatePurchaseOrder(Long purchaseId, PurchaseOrderDTO dto);


	PurchaseOrder createPurchaseOrderFromRequestDTO(ProcurementRequestDTO requestDTO, Supplier supplier);
}
