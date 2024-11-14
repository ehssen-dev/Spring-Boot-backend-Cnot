package tn.pfe.CnotConnectV1.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private String name;
    private String contactInformation;
    private String address;
    private String email;
    private Double onTimeDeliveryRate; // metric (0.0 to 1.0)
    private Double qualityControlRating; // metric (0.0 to 5.0)
    private int numberOfPastIssues;
    


}
