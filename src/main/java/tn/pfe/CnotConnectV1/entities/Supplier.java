package tn.pfe.CnotConnectV1.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
public class Supplier {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long supplierId;

    private String name;
    @Column(name = "contact_Information")
    private String contactInformation;
    private String address;
    private String email;
    @Column(name = "on_Time_Delivery_Rate")
    private Double onTimeDeliveryRate; //  metric (0.0 to 1.0)
    @Column(name = "quality_Control_Rating")
    private Double qualityControlRating; //  metric (0.0 to 5.0)
    @Column(name = "number_Of_Past_Issues")
    private int numberOfPastIssues;  //(number of unresolved issues)

    

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<PurchaseOrder> purchaseOrders;
  

}
