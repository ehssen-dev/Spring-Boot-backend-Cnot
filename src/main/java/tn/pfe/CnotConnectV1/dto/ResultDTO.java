package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDateTime;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
    private String winner;
    private String runnerUp;
    private String thirdPlace;
    private String scores;
    private String highlights;
    private String status;
    private LocalDateTime resultDate;
    private String comments;
    private Long gameId;
}
