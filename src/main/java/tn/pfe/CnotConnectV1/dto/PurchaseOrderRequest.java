package tn.pfe.CnotConnectV1.dto;

import lombok.Getter;
import lombok.Setter;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;

@Getter
@Setter
public class PurchaseOrderRequest {

    private PurchaseOrder purchaseOrder;
    private Long supplierId;
    private Long projectId;
    private Long invoiceId;
}
