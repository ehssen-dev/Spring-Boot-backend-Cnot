package tn.pfe.CnotConnectV1.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String identifier;
    private String username;
    private String email;
    private Long athleteId;
    
    public UserDTO(Long userId, String username, Long athleteId) {
        this.userId = userId;
        this.username = username;
        this.athleteId = athleteId;
    }
}