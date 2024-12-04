package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String name;
    private LocalDate startEvent;
    private LocalDate endEvent;
    private String description;
    private Long archiveId; 

}
