package tn.pfe.CnotConnectV1.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.Supplier;
import tn.pfe.CnotConnectV1.enums.OrderStatus;



@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findAllById(Iterable<Long> purchaseIds);

    List<PurchaseOrder> findByProject(Project project);
    List<PurchaseOrder> findByProject_ProjectId(Long projectId);    
    
    List<PurchaseOrder> findBySupplier(Supplier supplier);
    
    List<PurchaseOrder> findByStatus(OrderStatus status);
    
    List<PurchaseOrder> findByQuantityGreaterThanEqual(int quantity);
    
    List<PurchaseOrder> findByPurchaseDateBetween(Date startDate, Date endDate);
    
    List<PurchaseOrder> findByExpectedDeliveryDateBetween(Date startDate, Date endDate);
    
//	List<PurchaseOrder> findByProjectSupplierAndTotalAmountGreaterThanEqual(Supplier supplier, double amount);
    List<PurchaseOrder> findBySupplierAndTotalAmountGreaterThanEqual(Supplier supplier, double totalAmount);
    PurchaseOrder findBySupplierAndDescriptionAndQuantity(Supplier supplier, String description, Integer quantity);

}