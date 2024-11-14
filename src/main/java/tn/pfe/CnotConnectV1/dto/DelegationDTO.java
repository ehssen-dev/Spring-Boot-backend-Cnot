package tn.pfe.CnotConnectV1.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DelegationDTO {

    private Long delegationId;

    @NotBlank
    @Size(max = 100)
    private String delegationName;

    private String country;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

}
