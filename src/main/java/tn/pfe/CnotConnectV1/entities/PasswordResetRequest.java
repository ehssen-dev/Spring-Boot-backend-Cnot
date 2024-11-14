package tn.pfe.CnotConnectV1.entities;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String newPassword;
    private String confirmPassword;
}
